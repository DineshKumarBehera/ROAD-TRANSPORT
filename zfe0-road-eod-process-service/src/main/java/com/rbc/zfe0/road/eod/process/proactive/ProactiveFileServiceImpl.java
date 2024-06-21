package com.rbc.zfe0.road.eod.process.proactive;

import com.jcraft.jsch.*;
import com.rbc.zfe0.road.eod.dto.IIssueExportSummary;
import com.rbc.zfe0.road.eod.dto.ITransferItemExportSummary;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.persistence.entity.DeliveryInstruction;
import com.rbc.zfe0.road.eod.persistence.entity.TransferAgent;

import com.rbc.zfe0.road.eod.persistence.repository.DeliveryInstructionRepository;
import com.rbc.zfe0.road.eod.persistence.repository.IssueRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferAgentRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import com.rbc.zfe0.road.eod.utils.RbcUtil;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ProactiveFileServiceImpl implements ProactiveFileService {

    @Autowired
    TransferAgentRepository transferAgentRepository;
    @Autowired
    DeliveryInstructionRepository deliveryInstructionRepository;
    @Autowired
    TransferItemRepository transferItemRepository;
    @Autowired
    IssueRepository issueRepository;

    @Value("${rbc.road.scheduler.proactive.proactive-file-path}")
    private String proactiveFilePath;

    private static SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy");

    @Value("${rbc.road.nas.host}")
    private String host;

    @Value("${rbc.road.nas.username}")
    private String username;

    @Value("${rbc.road.nas.password}")
    private String password;

    @Value("${rbc.road.nas.remote-file-path}")
    private String remoteFilePath;

    static String PROACTIVE_FILE_NAME = "ROADOTT_Full_stripped_";
    static String FILE_EXTENSION = ".csv";

    public void generateOTTFullStrippedFile(String statusCode) throws ServiceLinkException, JSchException, SftpException {
        long startTime = System.currentTimeMillis();
        log.info("Proactive file Started : {} milliseconds", startTime);

        boolean response;

        try {
            response = generateOTTFile(statusCode);
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: Intact file records job exception " + t.getMessage(), t);
            log.error("EOD process failed while processing Intact file records - ", t);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        log.info("Time taken for Proactive file : {} milliseconds", totalTime);
    }

    /**
     * Fetch Data
     * Write each line to ArrayList
     * Call Writer
     *
     * @param statusCode
     * @return
     * @throws ServiceLinkException
     * @throws JSchException
     * @throws SftpException
     */
    private boolean generateOTTFile(String statusCode) throws ServiceLinkException, JSchException, SftpException {
        List<ITransferItemExportSummary> transferItems = transferItemRepository.getTransferItemsByStatusCode(statusCode);
        List<IIssueExportSummary> issues = issueRepository.findTransferItemsWithIssueByStatusCode(statusCode);

        StringBuilder header = new StringBuilder();
        ArrayList<String> lstOfDataLines = new ArrayList<String>();

        header.append("Item ID,");
        header.append("Item Status,");
        header.append("Branch,");
        header.append("Alt Branch Cd,");
        header.append("Rep ID,");
        header.append("Item Acct #,");
        header.append("Acct Tax ID,");
        header.append("Item Recd,");
        header.append("Confirm Recd,");
        header.append("Confirm Sent,");
        header.append("NI Recd,");
        header.append("Item Type,");
        header.append("Sec #,");
        header.append("CUSIP,");
        header.append("Sec Desc,");
        header.append("Qty,");
        header.append("Certificate,");
        header.append("Cert Date,");
        header.append("Cert Nbr,");
        header.append("Transfer Type,");
        header.append("Agent,");
        header.append("Delivery Instruction,");
        header.append("Disposition,");
        header.append("Mail Receipt,");
        header.append("Reg Tax ID,");
        header.append("NI Registration,");
        header.append("NI Debit,");
        header.append("NI Credit,");
        header.append("NI Batch,");
        header.append("NI Entry,");
        header.append("Value,");
        header.append("Check Amt,");
        header.append("Check/Wire #,");
        header.append("Transferred Dt,");
        header.append("Control ID,");
        header.append("Last Modified Dt,");
        header.append("Last Updated By");
        lstOfDataLines.add(header.toString());

        Map<Integer, ITransferItemExportSummary> transferItemsMap = new LinkedHashMap<>();
        for (ITransferItemExportSummary transferItem : transferItems) {
            Integer transferItemId = transferItem.getTransferItemId();
            transferItemsMap.put(transferItemId, transferItem);
        }

        Map<Integer, IIssueExportSummary> issueMap = new LinkedHashMap<>();
        for (IIssueExportSummary issue : issues) {
            Integer transferItemId = issue.getTransferItem().getTransferItemId();
            issueMap.put(transferItemId, issue);
        }

        // Create a map to store transfer agents
        Map<Integer, TransferAgent> transferAgentMap = new HashMap<>();
        // Retrieve all transfer agents
        List<TransferAgent> transferAgents = transferAgentRepository.findAll();
        // Populate the transfer agent map
        for (TransferAgent transferAgent : transferAgents) {
            transferAgentMap.put(transferAgent.getTransferAgentId(), transferAgent);
        }

        Map<Integer, DeliveryInstruction> deliveryInstructionNameMap = new HashMap<>();
        List<DeliveryInstruction> deliveryInstructionsAll = deliveryInstructionRepository.findAll();
        // Populate the delivery instruction name map
        for (DeliveryInstruction deliveryInstruction : deliveryInstructionsAll) {
            if (deliveryInstruction != null) {
                deliveryInstructionNameMap.put(deliveryInstruction.getDeliveryInstructionId(), deliveryInstruction);
            }
        }

        // Set to store unique transfer item IDs
        Set<Integer> uniqueTransferItemIds = new HashSet<>();

        // Iterate through transfer item IDs
        for (Integer transferItemId : transferItemsMap.keySet()) {
            if (!uniqueTransferItemIds.contains(transferItemId)) {
                uniqueTransferItemIds.add(transferItemId);
                ITransferItemExportSummary transferItem = transferItemsMap.get(transferItemId);
                IIssueExportSummary niIssue = issueMap.get(transferItemId);
                BigDecimal quantityTi = transferItem.getOriginalQuantity();
                double quantityValueTi = quantityTi != null ? quantityTi.doubleValue() : 0.0;

                // Retrieve the transfer agent from the map
                TransferAgent transferAgent = transferAgentMap.get(transferItem.getTransferAgentId());
                String transferAgentName = transferAgent.getTransferAgentName();
                String deliveryNm = null;
                String deliveryNmIss = null;
                DeliveryInstruction deliveryInstruction = deliveryInstructionNameMap.get(transferItem.getDeliveryInstructionId());
                if (deliveryInstruction != null) {
                    deliveryNm = transferItem.getDeliveryInstructionName();
                }

                deliveryInstruction = deliveryInstructionNameMap.get(niIssue.getTransferItem().getDeliveryInstruction().getDeliveryInstructionName());
                if (deliveryInstruction != null) {
                    deliveryNmIss = niIssue.getTransferItem().getDeliveryInstruction().getDeliveryInstructionName();
                }
                StringBuilder oiDataRow = new StringBuilder();

                oiDataRow.append(transferItem.getTransferItemId() != null ? transferItem.getTransferItemId() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getStatusCode() != null ? transferItem.getStatusCode() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getAdpBranchCode() != null ? transferItem.getAdpBranchCode() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getAltBranchCode() != null ? transferItem.getAltBranchCode() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getRepId() != null ? transferItem.getRepId() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getAdpAccountNumber() != null ? transferItem.getAdpAccountNumber() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getAccountTaxId() != null ? transferItem.getAccountTaxId() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getReceivedDt() != null ? RbcUtil.convertDate(transferItem.getReceivedDt()) : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getConfirmationReceivedDt() != null ? RbcUtil.convertDate(transferItem.getConfirmationReceivedDt()) : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getConfirmationSentDt() != null ? RbcUtil.convertDate(transferItem.getConfirmationSentDt()) : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getIssue().getEntryDate() != null ? RbcUtil.convertDate(transferItem.getIssue().getEntryDate()) : "");
                oiDataRow.append(",");

                oiDataRow.append("OI");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getAdpSecurityNumber() != null ? transferItem.getAdpSecurityNumber() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getOriginalCusIp() != null ? transferItem.getOriginalCusIp() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getOriginalSecurityDescr() != null ? transferItem.getOriginalSecurityDescr() : "");
                oiDataRow.append(",");

                oiDataRow.append(quantityValueTi);
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getIssue().getCertificateNumber() != null ? transferItem.getIssue().getCertificateNumber() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getIssue().getCertificateDate() != null ? transferItem.getIssue().getCertificateDate() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getIssue().getCertificateNumber() != null ? transferItem.getIssue().getCertificateNumber() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getTransferTypeCode() != null ? transferItem.getTransferTypeCode() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferAgentName);
                oiDataRow.append(",");

                if (deliveryNm != null) {
                    oiDataRow.append(deliveryNm);
                    oiDataRow.append(",");

                } else {
                    oiDataRow.append("");
                    oiDataRow.append(",");
                }

                oiDataRow.append(transferItem.getDispositionCode() != null ? transferItem.getDispositionCode() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getMailReceiptNumber() != null ? transferItem.getMailReceiptNumber() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getRegistrationTaxId() != null ? transferItem.getRegistrationTaxId() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getRegistrationDesc() != null ? transferItem.getRegistrationDesc() : "");
                oiDataRow.append(",");

                oiDataRow.append(transferItem.getIssue().getDleAccountCheckDigit() != null ? transferItem.getIssue().getDleAccountCheckDigit() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getIssue().getCseAccountCheckDigit() != null ? transferItem.getIssue().getCseAccountCheckDigit() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getIssue().getBatchCode() != null ? transferItem.getIssue().getBatchCode() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getIssue().getEntryCode() != null ? transferItem.getIssue().getEntryCode() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getIssue().getInsuranceValue() != null ? transferItem.getIssue().getInsuranceValue().toString() : "");
                oiDataRow.append(",");

                BigDecimal checkAmount = transferItem.getIssue().getCheckAmount();
                if (checkAmount != null) {
                    oiDataRow.append(checkAmount.toString());
                    oiDataRow.append(",");
                } else {
                    oiDataRow.append("");
                    oiDataRow.append(",");
                }
                oiDataRow.append(transferItem.getIssue().getCheckNumber() != null ? transferItem.getIssue().getCheckNumber() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getTransferredDt() != null ? RbcUtil.convertDate(transferItem.getTransferredDt()) : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getControlId() != null ? transferItem.getControlId() : "");
                oiDataRow.append(",");
                oiDataRow.append(transferItem.getLastUpdateDt() != null ? RbcUtil.convertDate(transferItem.getLastUpdateDt()) : "");
                oiDataRow.append(",");
                //oiDataRow.append(transferItem.getLastUpdateName() != null ? transferItem.getLastUpdateName() : "");

                String oiLastUpdateName = transferItem.getLastUpdateName() != null ? transferItem.getLastUpdateName() : "";
                String oiLastUpdateNameWithCommaPreserved = "\""+ oiLastUpdateName + "\"";
                oiDataRow.append(oiLastUpdateNameWithCommaPreserved);

                lstOfDataLines.add(oiDataRow.toString());

                if (niIssue != null) {
                    Integer issueId = niIssue.getIssueID();
                    if (!uniqueTransferItemIds.contains(issueId)) {
                        uniqueTransferItemIds.add(issueId);
                        // Add the NI record
                        StringBuilder niDataRow = new StringBuilder();

                        niDataRow.append(transferItem.getTransferItemId() != null ? transferItem.getTransferItemId() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getStatusCode() != null ? transferItem.getStatusCode() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getAdpBranchCode() != null ? transferItem.getAdpBranchCode() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getAltBranchCode() != null ? transferItem.getAltBranchCode() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getRepId() != null ? transferItem.getRepId() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getAdpAccountNumber() != null ? transferItem.getAdpAccountNumber() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getAccountTaxId() != null ? transferItem.getAccountTaxId() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getReceivedDt() != null ? RbcUtil.convertDate(transferItem.getReceivedDt()) : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getConfirmationReceivedDt() != null ? RbcUtil.convertDate(transferItem.getConfirmationReceivedDt()) : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getConfirmationSentDt() != null ? RbcUtil.convertDate(transferItem.getConfirmationSentDt()) : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getIssue().getEntryDate() != null ? RbcUtil.convertDate(transferItem.getIssue().getEntryDate()) : "");
                        niDataRow.append(",");

                        niDataRow.append("NI");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getAdpSecurityNumber() != null ? transferItem.getAdpSecurityNumber() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getOriginalCusIp() != null ? transferItem.getOriginalCusIp() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getOriginalSecurityDescr() != null ? transferItem.getOriginalSecurityDescr() : "");
                        niDataRow.append(",");

                        niDataRow.append(quantityValueTi);
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getIssue().getCertificateNumber() != null ? transferItem.getIssue().getCertificateNumber() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getIssue().getCertificateDate() != null ? transferItem.getIssue().getCertificateDate() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getIssue().getCertificateNumber() != null ? transferItem.getIssue().getCertificateNumber() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferItem.getTransferTypeCode() != null ? transferItem.getTransferTypeCode() : "");
                        niDataRow.append(",");

                        niDataRow.append(transferAgentName);
                        niDataRow.append(",");

                        if (deliveryNm != null) {
                            niDataRow.append(deliveryNm);
                            niDataRow.append(",");

                        } else {
                            niDataRow.append("");
                            niDataRow.append(",");
                        }
                        niDataRow.append(transferItem.getDispositionCode() != null ? transferItem.getDispositionCode() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getMailReceiptNumber() != null ? transferItem.getMailReceiptNumber() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getRegistrationTaxId() != null ? transferItem.getRegistrationTaxId() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getRegistrationDesc() != null ? transferItem.getRegistrationDesc() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getIssue().getDleAccountCheckDigit() != null ? transferItem.getIssue().getDleAccountCheckDigit() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getIssue().getCseAccountCheckDigit() != null ? transferItem.getIssue().getCseAccountCheckDigit() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getIssue().getBatchCode() != null ? transferItem.getIssue().getBatchCode() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getIssue().getEntryCode() != null ? transferItem.getIssue().getEntryCode() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getIssue().getInsuranceValue() != null ? transferItem.getIssue().getInsuranceValue().toString() : "");
                        niDataRow.append(",");

                        if (checkAmount != null) {
                            niDataRow.append(checkAmount.toString());
                            niDataRow.append(",");
                        } else {
                            niDataRow.append("");
                            niDataRow.append(",");
                        }

                        niDataRow.append(transferItem.getIssue().getCheckNumber() != null ? transferItem.getIssue().getCheckNumber() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getTransferredDt() != null ? RbcUtil.convertDate(transferItem.getTransferredDt()) : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getControlId() != null ? transferItem.getControlId() : "");
                        niDataRow.append(",");
                        niDataRow.append(transferItem.getLastUpdateDt() != null ? RbcUtil.convertDate(transferItem.getLastUpdateDt()) : "");
                        niDataRow.append(",");

                        String niLastUpdateName = transferItem.getLastUpdateName() != null ? transferItem.getLastUpdateName() : "";
                        //niDataRow.append(transferItem.getLastUpdateName() != null ? transferItem.getLastUpdateName() : "");
                        String niLastUpdateNameWithCommaPreserved = "\""+ niLastUpdateName + "\"";
                        niDataRow.append(niLastUpdateNameWithCommaPreserved);

                        lstOfDataLines.add(niDataRow.toString());

                    }
                }
            }
        }

        writeFile(lstOfDataLines);
        return true;
    }


    /**
     * Create File
     * Upload to NAS
     * @param recs
     * @throws ServiceLinkException
     * @throws SftpException
     * @throws JSchException
     */
    public void writeFile(List recs) throws ServiceLinkException, SftpException, JSchException {
        log.info("Write Proactive File");
        String fileName = PROACTIVE_FILE_NAME + todaysDate() + FILE_EXTENSION;
        createAndUploadFileToNAS(fileName, recs);
    }

/**
 * Opens NAS COnnection
 * Create the file and write content to it
 * Upload the file to NAS Storage
 * @param proactiveOTTFileName
 * @param recs
 */private void createAndUploadFileToNAS(String proactiveOTTFileName, List recs) throws ServiceLinkException{
    String fullyQualifiedFileName = host + remoteFilePath + proactiveOTTFileName;
    try {
        NtlmPasswordAuthentication ntlmAuth = new NtlmPasswordAuthentication("", username, password);
        SmbFile smbFile = new SmbFile(fullyQualifiedFileName, ntlmAuth);

        SmbFileOutputStream sfos = new SmbFileOutputStream(smbFile);

        try {
            //Now write to File
            Iterator itr = recs.iterator();
            while (itr.hasNext()) {
                String rec = (String) itr.next();
                sfos.write(rec.getBytes());
                //New Line
                sfos.write("\n".getBytes());
            }
            log.info("Done writing Proactive File " + fullyQualifiedFileName);
        }

        catch (Exception e) {
            log.info("Proactive Exception in writing the file Location 1 " + e.getLocalizedMessage());
            throw new ServiceLinkException(e);
        }
        finally {
            try {
                sfos.flush();
                sfos.close();
            } catch (IOException exp) {
                throw new ServiceLinkException(exp);
            }
        }
    }

    catch (Exception e) {
        log.info("Proactive Exception in connecting to NAS " + e.getLocalizedMessage());
        throw new ServiceLinkException(e);
    }
}


    public String todaysDate() {
        LocalDate dateObj = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        return dateObj.format(formatter);
    }
}