package com.rbc.zfe0.road.eod.process.intact;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.persistence.dao.BatchEntryDAO;
import com.rbc.zfe0.road.eod.persistence.entity.Issue;
import com.rbc.zfe0.road.eod.persistence.repository.IssueRepository;
import com.rbc.zfe0.road.eod.process.handler.mapper.BatchEntryMapper;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodIntactItem;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;
import com.rbc.zfe0.road.eod.process.handler.mapper.IssueMap;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.process.handler.EodFilesWriter;
import com.rbc.zfe0.road.eod.process.handler.EodTransit;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class IntactGenerator {

    private static SimpleDateFormat fmt = new SimpleDateFormat("MMddyyHHmmss");
    private static SimpleDateFormat fmt1 = new SimpleDateFormat("MMddyy");
    private static String FILE_DELIMITER = ",";

    @Autowired
    EodTransit eodTransit;

    @Autowired
    BatchEntryDAO batchEntryDAO;

    @Autowired
    EodFilesWriter eodFilesWriter;

    @Autowired
    IssueRepository issueRepository;

    public void generateIntactFile(Date lastEodDate, List errorList) throws ServiceLinkException, RoadException, JSchException, SftpException {
        log.info("Generate Intact File.");
        try{
            List items = null;
            items = getEodTransitRecordsIntact(lastEodDate);
            Map transferItemMap = createTransferItemMap(items);
            generateIntactRecords(transferItemMap, eodTransit.getOttUU97List(lastEodDate));
        } catch(Throwable t) {
            RoadException re = new RoadException("EOD: Intact file records job exception " + t.getMessage(), t);
            errorList.add(re);
            log.error("EOD process failed while processing Intact file records - ", t);
        }
    }

    private void generateIntactRecords(Map transferItemMap, List uu97itemList) throws ServiceLinkException, RoadException, SftpException, JSchException {
        log.info("Generate Intact Records.");
        List intactRecs = getIntactRecords(transferItemMap);
        intactRecs.addAll(getUU97IntactRecords(uu97itemList));
        generateIntactFile(intactRecs);
    }


    //get EOD Transit records for Intact file
    private List getEodTransitRecordsIntact(Date lastEodDate) throws ServiceLinkException {
        log.info("Get EOD Transit Records Intact.");
        List items = new ArrayList();
        List cewCloseList = null;
        List partialClosedList = null;
        items = eodTransit.getOTTRecords(lastEodDate);
        items.addAll(eodTransit.getClosedRecords(lastEodDate));
        cewCloseList = eodTransit.getCewCloseList(lastEodDate);
        if (!Utility.isEmpty(cewCloseList)) {
            items.addAll(eodTransit.checkCloseForRejected(cewCloseList));
        }
        partialClosedList = eodTransit.getPartialClosedItems(lastEodDate);
        if (!Utility.isEmpty(partialClosedList)) {
            items.addAll(eodTransit.changeStatusForPartiallyClosedItems(partialClosedList));
        }
        return items;
    }

    private Map createTransferItemMap(List items) {
        log.info("Create Transfer Item Map.");
        Iterator itemItr = items.iterator();
        EodTransferItem eodItem = null;
        IssueMap issueMap = null;
        ArrayList cashIssues = null;
        ArrayList securityIssues = null;
        Map transferItemMap = new HashMap();

        while (itemItr.hasNext()) {
            securityIssues = new ArrayList();
            cashIssues = new ArrayList();
            eodItem = (EodTransferItem) itemItr.next();
            if (eodItem.getCashEntryFlag() == 1) {
                cashIssues.add(eodItem);
            } else {
                securityIssues.add(eodItem);
            }
            if (transferItemMap.keySet().contains(eodItem.getTransferItemId())) {
                issueMap = (IssueMap) transferItemMap.get(eodItem.getTransferItemId());
            } else {
                issueMap = new IssueMap();
                transferItemMap.put(eodItem.getTransferItemId(), issueMap);
            }
            issueMap.getCashIssues().addAll(cashIssues);
            issueMap.getSecurityIssue().addAll(securityIssues);
        }
        return transferItemMap;
    }

/*
 * Prepare intact records from the TransferItems.
 * This method is for creating relations between parent original issue and new issues.
 * In some cases business rules suggest to skip entries for Batch and Entry codes which
 * takes out shares from OI issues(we should not take out shares twice fron OI issues).
 */
private List getIntactRecords(Map transferItemMap) throws RoadException, ServiceLinkException {
    log.info("Get Intact Records");
    EodTransferItem item = null;

    IssueMap mapEntry = null;
    List intactEntries = new ArrayList();
    Iterator itr = null;
    List cashIssues = null;
    List securityIssues = null;
    Iterator issueItr = null;
    int issueCount = 0;


    if (transferItemMap.size() > 0) {
        itr = transferItemMap.keySet().iterator();
        while (itr.hasNext()) {
            mapEntry = (IssueMap) transferItemMap.get(itr.next());
            cashIssues = mapEntry.getCashIssues();
            securityIssues = mapEntry.getSecurityIssue();
            if (!Utility.isEmpty(cashIssues)) {
                issueCount = 0;
                issueItr = cashIssues.iterator();
                while (issueItr.hasNext()) {
                    issueCount++;
                    item = (EodTransferItem) issueItr.next();
                    intactEntries.addAll(getIntactRecords(item, issueCount));
                }
            }
            if (!Utility.isEmpty(securityIssues)) {
                issueItr = securityIssues.iterator();
                issueCount = 0;
                while (issueItr.hasNext()) {
                    issueCount++;
                    item = (EodTransferItem) issueItr.next();
                    intactEntries.addAll(getIntactRecords(item, issueCount));
                }
            }
        }
    }
    return intactEntries;
}

    //prepare intact records from the TransferItems
    private List getIntactRecords(EodTransferItem item, int issueCount) throws RoadException, ServiceLinkException {
        log.info("Get Intact Records");
        List intactEntries = new ArrayList();
        Iterator batchEntryItr = null;
        BatchEntryMapper batchEntry = null;
        List batchEntryCodes = new ArrayList();
        batchEntryCodes = batchEntryDAO.getBatchEntryCodes(item, issueCount);
        if (!Utility.isEmpty(batchEntryCodes)) {
            batchEntryItr = batchEntryCodes.iterator();
            while (batchEntryItr.hasNext()) {
                batchEntry = (BatchEntryMapper) batchEntryItr.next();
                intactEntries.add(batchEntryDAO.getIntactItem(item, batchEntry));
                updateIssue(item, batchEntry);
            }
        }
        return intactEntries;
    }

    private List getUU97IntactRecords(List items) throws ServiceLinkException {
        log.info("Get UU97 Intact Records");
        EodTransferItem item = null;
        List intactEntries = new ArrayList();
        Iterator itr = null;
        Iterator batchEntryItr = null;
        BatchEntryMapper batchEntry = null;
        List batchEntryCodes = new ArrayList();

        if (!Utility.isEmpty(items)) {
            itr = items.iterator();
            while (itr.hasNext()) {
                item = (EodTransferItem) itr.next();
                batchEntryCodes = batchEntryDAO.getUU97BatchEntryCodes(item);
                batchEntryItr = batchEntryCodes.iterator();
                while (batchEntryItr.hasNext()) {
                    batchEntry = (BatchEntryMapper) batchEntryItr.next();
                    intactEntries.add(batchEntryDAO.getIntactItem(item, batchEntry));
                    updateIssue(item, batchEntry);
                }
            }
        }
        return intactEntries;
    }
    /**
     * ROAD considers following types of Recs for INTACT generation
     * - Debit_Cash posting
     * - Credit_Memo
     * - Debit_Memo
     * - Credit_Stock&Money
     * - Debit_Stock&Money
     */
    private void generateIntactFile(List items) throws ServiceLinkException, JSchException, SftpException {
        log.info("Generate Intact file");
        Iterator itmItr = items.iterator();
        ArrayList intactRecs = new ArrayList();

        EodIntactItem eodItem = null;
        String transactionType = "";
        ArrayList intactFile = new ArrayList();
        while (itmItr.hasNext()) {
            eodItem = (EodIntactItem) itmItr.next();
            transactionType = eodItem.getTransactionType();
            if (transactionType.equalsIgnoreCase(Constants.INTACT_REC_CREDIT_MEMO)) {
                intactRecs.add(fillCreditMemoRec(eodItem));
            } else if (transactionType.equalsIgnoreCase(Constants.INTACT_REC_DEBIT_MEMO)) {
                intactRecs.add(fillDebitMemoRec(eodItem));
            } else if (transactionType.equalsIgnoreCase(Constants.INTACT_REC_CREDIT_STOCK_MONEY)) {
                intactRecs.add(fillCreditStockAndMoney(eodItem));
            } else if (transactionType.equalsIgnoreCase(Constants.INTACT_REC_DEBIT_STOCK_MONEY)) {
                intactRecs.add(fillDebitStockAndMoney(eodItem));
            } else if (transactionType.equalsIgnoreCase(Constants.INTACT_REC_DEBIT_CASH_POSTING)) {
                intactRecs.add(fillDebitCashPostingRec(eodItem));
            } else {
                log.error("EOD Process unable to process record type  - " + eodItem.getTransactionType());
            }
        }
        intactFile.add(generateIntactHeader());
        intactFile.addAll(intactRecs);
        intactFile.add(generateIntactFooter(Utility.getStringFromInt(items.size())));


        eodFilesWriter.writeINTACT(intactFile);

        writeIntactLogFile(items);

    }

    private String generateIntactHeader() {
        log.info("Generate Intact Header");
        String headerStr = Constants.INTACT_HEADER;
        headerStr = headerStr.substring(0, 5) + fmt1.format(new Date()) + fmt.format(new Date()) + headerStr.substring(23, headerStr.length());
        return headerStr;
    }

    private String generateIntactFooter(String size) {
        log.info("Generate Intact Footer");
        String footer = Constants.INTACT_FOOTER;
        footer = footer.substring(0, 8) + StringUtils.leftPad(size, 11, '0') + fmt1.format(new Date()) + fmt.format(new Date()) + footer.substring(37, footer.length());
        return footer;
    }

    private String fillCreditMemoRec(EodTransferItem eodItem) {
        String str = fillCommonColumns(Constants.CREDIT_MEMO_REC, eodItem, "C");
        return getMemoRecord(eodItem, str);
    }

    private String fillDebitMemoRec(EodTransferItem eodItem) {
        String str = fillCommonColumns(Constants.DEBIT_MEMO_REC, eodItem, "D");
        return getMemoRecord(eodItem, str);
    }

    private String getMemoRecord(EodTransferItem eodItem, String record) {
        String str = record;
        String qty = getQuantity(eodItem);
        str = str.substring(0, 191) + qty + str.substring(208, str.length());
        str = str.substring(0, 293) + Constants.SECURITY_TYPE + str.substring(294, str.length());
        String securityID = getSecurityId(eodItem);
        str = str.substring(0, 294) + securityID + str.substring(309, str.length());
        return str;
    }

    private String fillCreditCashPostingRec(EodTransferItem eodItem) {
        String str = fillCommonColumns(Constants.CREDIT_CASH_POSTING_REC, eodItem, "C");
        return getCashPostingRecord(eodItem, str);
    }
    private String fillDebitCashPostingRec(EodTransferItem eodItem) {
        String str = fillCommonColumns(Constants.DEBIT_CASH_POSTING_REC, eodItem, "D");
        return getCashPostingRecord(eodItem, str);
    }

    private String getCashPostingRecord(EodTransferItem eodItem, String record) {
        String str = record;
        String amount = getCheckAmount(eodItem);
        str = str.substring(0, 869) + amount + str.substring(886, str.length());
        return str;
    }

    private String fillCreditStockAndMoney(EodTransferItem eodItem) {
        String str = fillCommonColumns(Constants.CREDIT_STOCK_MONEY_REC, eodItem, "C");
        return getStockAndMoneyRecord(eodItem, str);
    }

    private String fillDebitStockAndMoney(EodTransferItem eodItem) {
        String str = fillCommonColumns(Constants.DEBIT_STOCK_MONEY_REC, eodItem, "D");
        return getStockAndMoneyRecord(eodItem, str);
    }

    private String getStockAndMoneyRecord(EodTransferItem eodItem, String record) {
        String str = record;
        String qty = getQuantity(eodItem);
        String amount = getCheckAmount(eodItem);
        str = str.substring(0, 191) + qty + str.substring(208, str.length());
        str = str.substring(0, 293) + Constants.SECURITY_TYPE + str.substring(294, str.length());
        String securityID = getSecurityId(eodItem);
        str = str.substring(0, 294) + securityID + str.substring(309, str.length());
        str = str.substring(0, 869) + amount + str.substring(886, str.length());
        return str;
    }

    //ADP asked for not to send discription in the Intact record.
    private String getSecurityDescription(EodTransferItem eodItem) {
        String securityDescription = "";
//        if(eodItem.getCashEntryFlag() ==1){
//            securityDescription = eodItem.getOrgSecurityDescr();
//        }else{
//            securityDescription = eodItem.getSecurityDescr();
//        }
//        if(!Utility.isEmpty(securityDescription)){
//           securityDescription = securityDescription.length() > 30?
//                    securityDescription.substring(0,30) : securityDescription;
//        }
        return StringUtils.rightPad(securityDescription, 30);
    }

    private String fillCommonColumns(String rec, EodTransferItem item, String type) {
        String recStr = rec;
        recStr = recStr.substring(0, 19) + item.getAdpAccountNumber() + recStr.substring(29, recStr.length());
        recStr = recStr.substring(0, 208) + StringUtils.rightPad(item.getBatchCode().trim(), 3) + recStr.substring(211, recStr.length());
        recStr = recStr.substring(0, 211) + StringUtils.rightPad(item.getEntryCode().trim(), 9) + recStr.substring(220, recStr.length());


        //  item.setControlId("CONTROLIDFIELD");

        if (item.getControlId() != null) {


            recStr = recStr.substring(0, 1486) + StringUtils.rightPad(item.getControlId().trim(), 29) + recStr.substring(1515, recStr.length());

        }
        recStr = recStr.substring(0, 1933) + getSecurityDescription(item) + recStr.substring(1963, recStr.length());

        return recStr;
    }
    private String getQuantity(EodTransferItem eodItem) {
        String quantity = "0";
        int index = 0;
        if (!Utility.isEmpty(eodItem.getQuantity())) {
            quantity = eodItem.getQuantity().toString();
            index = quantity.indexOf('.'); //remove . for mainframe
            if (index > 0) {
                quantity = quantity.substring(0, index) + quantity.substring(index + 1, quantity.length() - 1);
            }
        }
        return StringUtils.leftPad(quantity, 17, '0');
    }

    private String getCheckAmount(EodTransferItem eodItem) {
        String amountReceived = "0";
        int index = 0;
        if (!Utility.isEmpty(eodItem.getCheckAmount())) {
            amountReceived = eodItem.getCheckAmount().toString();
            index = amountReceived.indexOf('.'); //remove . for mainframe
            if (index > 0) {
                amountReceived = amountReceived.substring(0, index) +
                        StringUtils.rightPad(amountReceived.substring(index + 1, index + 3), 2, '0');
            }
        }
        return StringUtils.leftPad(amountReceived, 17, '0');
    }

    private String getSecurityId(EodTransferItem eodItem) {
        String adpSecurityNumber = "";
        if (eodItem.getCashEntryFlag() == 1) {
            adpSecurityNumber = StringUtils.rightPad(eodItem.getOriginalAdpSecurityNumber().toUpperCase(), 15);
        }

        adpSecurityNumber = StringUtils.rightPad(StringUtils.trimToEmpty(eodItem.getAdpSecurityNumber()), 15);
        if (adpSecurityNumber.length() != 0) {
            adpSecurityNumber.toUpperCase();
        }
        return adpSecurityNumber;
    }

    private List getIntactLogRecs(List eodItems) {
        EodIntactItem item = null;
        List intactLogRecs = new ArrayList();
        intactLogRecs.add(getIntactHeader());
        Iterator eodItr = eodItems.iterator();
        while (eodItr.hasNext()) {
            item = (EodIntactItem) eodItr.next();
            intactLogRecs.add(getIntactRec(item));
        }
        return intactLogRecs;
    }

    //header for the IntactLog file
    private String getIntactHeader() {
        StringBuffer header = new StringBuffer();
        header.append("Status").append(FILE_DELIMITER).append("Batch").append(FILE_DELIMITER);
        header.append("ENTRY").append(FILE_DELIMITER).append("Entry Type").append(FILE_DELIMITER).append("Memo-Type").append(FILE_DELIMITER).append("Orig Sec#").append(FILE_DELIMITER);
        header.append("Cust Acct#").append(FILE_DELIMITER);
        header.append("Credit").append(FILE_DELIMITER).append("Orig CUSIP").append(FILE_DELIMITER).append("Orig Sec Desc").append(FILE_DELIMITER);
        header.append("Orig Qty").append(FILE_DELIMITER).append("New Sec#").append(FILE_DELIMITER);
        header.append("new CUSIP").append(FILE_DELIMITER).append("New Sec Desc").append(FILE_DELIMITER);
        header.append("New Qty").append(FILE_DELIMITER).append("Doller").append(FILE_DELIMITER).append("Transferred Date").append(FILE_DELIMITER);
        header.append("Confirmation Revd").append(FILE_DELIMITER).append("Last Modified Date").append(FILE_DELIMITER).append("Last Modified By");

        return header.toString();
    }
    //prepare record for the the log file
    private String getIntactRec(EodIntactItem item) {
        StringBuffer header = new StringBuffer();
        header.append(getString(item.getStatusCode(), 4)).append(FILE_DELIMITER).append(getString(item.getBatchCode(), 2)).append(FILE_DELIMITER);
        header.append(getString(item.getEntryCode(), 3)).append(FILE_DELIMITER).append(item.getTransferTypeCode()).append(FILE_DELIMITER);
        header.append(item.getTransactionType()).append(FILE_DELIMITER);
        header.append(getString(item.getOriginalAdpSecurityNumber(), 7)).append(FILE_DELIMITER);
        header.append(item.getAdpAccountNumber()).append(FILE_DELIMITER);
        //header.append(getString(getString(item.getDLEAccountNumber())+getString(item.getDLEAccountType())+ getString(item.getDLEAccountCheckDigit()),10)).append(FILE_DELIMITER);
        header.append(getString(getString(item.getCSEAccountNumber()) + getString(item.getCSEAccountType()) + getString(item.getCSEAccountCheckDigit()), 10)).append(FILE_DELIMITER);
        header.append("   ").append(getString(item.getOriginalCusIP(), 9)).append(FILE_DELIMITER);
        header.append(getString(item.getOrgSecurityDescr().trim(), 20)).append(FILE_DELIMITER);
        header.append(getString(item.getOriginalQuantity())).append(FILE_DELIMITER);
        header.append(item.getAdpSecurityNumber());
        header.append(FILE_DELIMITER).append(getString(item.getCusIP(), 9)).append(FILE_DELIMITER).append(getString(item.getSecurityDescr(), 15));
        header.append(FILE_DELIMITER).append(getString(item.getQuantity())).append(FILE_DELIMITER).append(getString(item.getInsuranceValue())).append(FILE_DELIMITER);
        header.append(getDate(item.getTransferredDate())).append(FILE_DELIMITER);
        header.append(getDate(item.getConfirmationReceivedDate())).append(FILE_DELIMITER);
        header.append(getDate(item.getLastUpdatedDate())).append(FILE_DELIMITER).append(item.getLastUpdatedName());

        return header.toString();
    }

    private String getString(String string) {
        String retStr = "";
        if (!Utility.isEmpty(string)) {
            retStr = string;
        }
        return retStr;
    }

    private String getString(String string, int len) {
        String retStr = "";
        if (!Utility.isEmpty(string)) {
            if (string.length() > len) {
                retStr = string.substring(0, len);
            } else {
                retStr = string;
            }
        }
        return StringUtils.rightPad(retStr, len);
    }

    private String getString(BigDecimal qty) {
        String qtyStr = "";
        if (qty != null) {
            qtyStr = qty.toString();
        }
        return StringUtils.rightPad(qtyStr, 12);
    }

    private String getDate(Date aDate) {
        String fmtDate = "";
        if (!Utility.isEmpty(aDate)) {
            fmtDate = fmt.format(aDate);
        }
        return StringUtils.leftPad(fmtDate, 10);
    }

    private void updateIssue(EodTransferItem eodItem, BatchEntryMapper batchEntry) {
        if (batchEntry.isDbUpdate()) {
            Optional<Issue> issue = issueRepository.findById(eodItem.getIssueId());
            if(issue.isPresent()) {
                issue.get().setBatchCode(batchEntry.getBatchCode());
                issue.get().setEntryCode(batchEntry.getEntryCode());
                issue.get().setLastUpdateName(Constants.APPNAME +"_EOD");
                issue.get().setLastUpdateDt(new Date());
                if(eodItem.getStatusCode().equalsIgnoreCase(Constants.STATUS_CLOSED)){
                    issue.get().setCloseFlag(1);
                }
                issueRepository.save(issue.get());
            }
        }
    }

    private void writeIntactLogFile(List eodItems) throws ServiceLinkException, JSchException, SftpException {
        eodFilesWriter.wrtieINTACTLog(getIntactLogRecs(eodItems));
    }


}