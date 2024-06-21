package com.rbc.zfe0.road.eod.process.cage;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.process.handler.EodFilesWriter;
import com.rbc.zfe0.road.eod.process.handler.EodTransit;
import com.rbc.zfe0.road.eod.helper.MFServiceHelper;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

@Component
@Slf4j
public class CageGenerator {

    @Value("${rbc.road.cage.box-location-for-cage}")
    private String boxLocationForCage;

    private String adpBranchCode = null;
    private String adpAccountNumber = null;
    private String adpAccountType = null;
    private String adpAccountCheckDigit = null;
    private String adpSecuirtyNumber = null;
    private String cusIP = null;
    private String TNST_TIME_STAMP = null;
    private String quantity = null;
    private static String TNST_BR = "097";
    private static String TNST_ACCT = "00021";
    private static String TNST_ACCT_TYPE = "1";
    private static String TNST_ACCT_CDG = "7";
    private static String TNST_STATUS = "C2";
    private static String TNST_STAT = "C";
    private static String TNST_STAT_SEQ = "2";
    private static String TNST_DEST = "DPD";
    private static String TNSTE_DEST_MAJOR = "DP";
    private static String TNSTE_DEST_MINOR = "D";
    private static String TNST_ROUTING_ID = "Y";
    private static String TNST_TYPE = "  ";
    private static String TNST_SETUP_CD = "     ";
    private static String TNST_SEQ = "         ";
    private static String TNST_LAST_UPDATE_PGM = "        ";
    private static String DEST_DESC = "DTC DEPOSIT    ";
    private static String ACCT_CURR_CDE = "$U";
    private static String OVRRD_OFAC_CHK_CD = "Y";
    private static String FILLER = "                            ";
    private static String ERROR_CODE = "  ";


    @Autowired
    TransferItemRepository transferItemRepository;

    @Autowired
    EodFilesWriter eodFilesWriter;

    @Autowired
    MFServiceHelper mfServiceHelper;

    @Autowired
    EodTransit eodTransit;

    public void generateCageRecords(Date lastEodDate, List errorList) throws ServiceLinkException, JSchException, SftpException {
        try {
            List items = null;
            items = getEodTransitRecordsCage(lastEodDate);
            buildCageFileEntries(items);
        } catch(Throwable t) {
            RoadException re = new RoadException("EOD: Cage file records job exception " + t.getMessage(), t);
            errorList.add(re);
            log.error("EOD process failed while processing Cage file records - ", t);
        }

    }

    //get EOD Transit Records for Cage file
    private List getEodTransitRecordsCage(Date lastEodDate) throws ServiceLinkException {
        List items = new ArrayList();
        List cewCloseList = null;
        List partialClosedList = null;
        items = eodTransit.getOTTRecords(lastEodDate);
        items.addAll(eodTransit.getClosedRecords(lastEodDate));
        cewCloseList = eodTransit.getCewCloseList(lastEodDate);
        if (!Utility.isEmpty(cewCloseList)) {
            items.addAll(eodTransit.checkCloseForRejected(cewCloseList));
        }
        //remove frozen accounts
        items.removeAll(eodTransit.getFreezedTransferItems(items));
        partialClosedList = eodTransit.getPartialClosedItems(lastEodDate);
        if (!Utility.isEmpty(partialClosedList)) {
            items.addAll(eodTransit.changeStatusForPartiallyClosedItems(partialClosedList));
        }
        return items;
    }

    //formates the date to YYYY-MM-DD-hh-ss-nnnnnnn
    private String getTimeStampString() {
        Timestamp time = new Timestamp((new Date()).getTime());
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        String year = Utility.getStringFromInt(c1.get(Calendar.YEAR));
        String month = StringUtils.leftPad(Utility.getStringFromInt(c1.get(Calendar.MONTH)), 2, '0');
        String day = StringUtils.leftPad(Utility.getStringFromInt(c1.get(Calendar.DATE)), 2, '0');
        String hours = StringUtils.leftPad(Utility.getStringFromInt(c1.get(Calendar.HOUR)), 2, '0');
        String min = StringUtils.leftPad(Utility.getStringFromInt(c1.get(Calendar.MINUTE)), 2, '0');
        String seconds = StringUtils.leftPad(Utility.getStringFromInt(c1.get(Calendar.SECOND)), 2, '0');
        String nonoSecs = Utility.getStringFromInt(time.getNanos()).toString().substring(0, 6);
        String timeStampString = year + "-" + month + "-" + day + "-" + hours + "." + min +
                "." + seconds + "." + nonoSecs;
        return timeStampString;
    }
    private void buildCageFileEntries(List transferItems) throws ServiceLinkException, SftpException, JSchException {
        List cageRecs = new ArrayList();
        Iterator itr = null;
        EodTransferItem item = null;
        if (!Utility.isEmpty(transferItems)) {
            itr = transferItems.iterator();
            while (itr.hasNext()) {
                item = (EodTransferItem) itr.next();
                if (!Utility.isEmpty(item.getCusIP())) {
                    if (!Utility.isEmpty(item.getCSEAccountNumber()) && (item.getCSEAccountNumber()
                            .equalsIgnoreCase(boxLocationForCage))) {

                        fillObj(item);
                        cageRecs.add(prepareRecord());
                    }
                } else {
                    log.error("CUSIP is Empty for Issue_id  - " + item.getIssueId());
                }
            }
        }
        writeCageRecords(cageRecs);
    }

    private void fillObj(EodTransferItem item) {
        this.adpAccountNumber = item.getAdpAccountNumber();
        this.adpAccountType = item.getAdpAccountType();
        this.adpAccountCheckDigit = item.getAdpAccountCheckDigit();
        this.adpBranchCode = item.getAdpBranchCode();
        this.quantity = StringUtils.leftPad(item.getQuantity().toString(), 17, '0');
        this.cusIP = item.getCusIP();
        this.adpSecuirtyNumber = item.getAdpSecurityNumber();
        this.TNST_TIME_STAMP = getTimeStampString();
    }

    //Fill all the values required for CAGE file record
    private String prepareRecord() {
        StringBuffer record = new StringBuffer();
        record.append(Constants.CLIENT_NUM_1).append(Constants.CLIENT_NUM_2);
        record.append(adpAccountNumber);
        record.append(adpAccountType).append(adpAccountCheckDigit);
        record.append(TNST_BR).append(TNST_ACCT).append(TNST_ACCT_TYPE);
        record.append(TNST_ACCT_CDG).append(TNST_STAT);
        record.append(TNST_STAT_SEQ).append(TNST_DEST).append("  ").append(TNST_ROUTING_ID);
        record.append(quantity).append(adpSecuirtyNumber).append(cusIP);
        record.append(getTimeStampString()).append(TNST_TYPE).append(TNST_SETUP_CD);
        record.append(TNST_SEQ).append(TNST_LAST_UPDATE_PGM);
        record.append(DEST_DESC).append(ACCT_CURR_CDE).append(OVRRD_OFAC_CHK_CD);
        record.append(FILLER).append(ERROR_CODE);
        return record.toString();
    }

    private void writeCageRecords(List recs) throws ServiceLinkException, JSchException, SftpException {
        eodFilesWriter.writeCAGE(recs);
    }
}