package com.rbc.zfe0.road.eod.process.icr;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rbc.zfe0.road.eod.process.handler.mapper.IEodTransferItem;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.process.handler.EodFilesWriter;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class IcrReportGenerator {

    @Autowired
    TransferItemRepository transferItemRepository;

    @Autowired
    EodFilesWriter eodFilesWriter;

    private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    public void generateReport() throws ServiceLinkException, JSchException, SftpException {
        log.info("Generate ICR report");
        List items = getICRReport();
        List recs = new ArrayList();
        Iterator itr = items.iterator();
        EodTransferItem item = null;
        while (itr.hasNext()) {
            item = (EodTransferItem) itr.next();
            recs.add(getReportRec(item));
        }
        eodFilesWriter.writeICRReport(recs);
    }

    private List getICRReport() {
        log.info("Get ICR report");
        List result;
        result = getICRReportRecords();
        result.addAll(getICRReportPendingRecords());
        List returnList = new ArrayList();
        for (int idx = 0; idx < result.size(); idx++) {
            EodTransferItem re = (EodTransferItem) result.get(idx);
            boolean found = false;
            for (int i = 0; i < returnList.size(); i++) {
                EodTransferItem tmp = (EodTransferItem) returnList.get(i);

                if (tmp.equalsICR(re)) {
                    if (tmp.getOriginalQuantity() == null) {
                        tmp.setOriginalQuantity(re.getOriginalQuantity());
                    } else if (re.getOriginalQuantity() != null) {
                        tmp.setOriginalQuantity(tmp.getOriginalQuantity().add(re.getOriginalQuantity()));
                    }

                    if (tmp.getQuantity() == null) {
                        tmp.setQuantity(re.getQuantity());
                    } else if (re.getQuantity() != null) {
                        tmp.setQuantity(tmp.getQuantity().add(re.getQuantity()));
                    }
                    found = true;
                    returnList.set(i, tmp);
                    break;
                }
            }
            if (!found) {
                returnList.add(re);
            }
        }
        return returnList;
    }

    private List<EodTransferItem> getICRReportRecords() {
        log.info("Get ICR Report Records");
        List<IEodTransferItem> list = transferItemRepository.getICRReportRecs();
        List<EodTransferItem> itemList = new ArrayList<>();
        for (IEodTransferItem ti : list) {
            boolean isCashEntry = (ti.getCashEntryFlag() == 1);
            EodTransferItem item = new EodTransferItem();
            if (ti.getTransferItemId() != null) {
                item.setTransferItemId(ti.getTransferItemId());
            }
            item.setCSEAccountNumber(ti.getCSEAccountNumber() == null ? "" : ti.getCSEAccountNumber());
            item.setCSEAccountType(ti.getCSEAccountType() == null ? "" : ti.getCSEAccountType());
            item.setCSEAccountCheckDigit(ti.getCSEAccountCheckDigit() == null ? "" : ti.getCSEAccountCheckDigit());
            item.setAdpAccountNumber(ti.getAdpAccountNumber() == null ? "" : ti.getAdpAccountNumber());
            item.setAdpAccountType(ti.getAdpAccountType() == null ? "" : ti.getAdpAccountType());
            item.setAdpAccountCheckDigit(ti.getAdpAccountCheckDigit() == null ? "" : ti.getAdpAccountCheckDigit());
            item.setAdpSecurityNumber(ti.getAdpSecurityNumber() == null ? "       " : ti.getAdpSecurityNumber());
            item.setOriginalAdpSecurityNumber(ti.getOriginalAdpSecurityNumber() == null ? "       " : ti.getOriginalAdpSecurityNumber());
            item.setOrgSecurityDescr(ti.getOrgSecurityDescr() == null ? "" : ti.getOrgSecurityDescr());
            item.setSecurityDescr(ti.getSecurityDescr() == null ? "" : ti.getSecurityDescr());
            item.setCusIP(ti.getCusIP() == null ? "" : ti.getCusIP());
            item.setOriginalCusIP(ti.getOriginalCusIP() == null ? "" : ti.getOriginalCusIP());
            item.setTransferredDate(ti.getTransferredDate());
            item.setConfirmationReceivedDate(ti.getConfirmationReceivedDate());
            item.setOriginalQuantity(ti.getOriginalQuantity());
            if (isCashEntry) {
                item.setQuantity(ti.getOriginalQuantity());
            } else {
                item.setQuantity(ti.getQuantity());
            }
            item.setLastUpdatedName(ti.getLastUpdatedName());
            item.setLastUpdatedDate(ti.getLastUpdatedDate());
            itemList.add(item);
        }
        return itemList;
    }
    private List<EodTransferItem> getICRReportPendingRecords() {
        log.info("Get ICR Report Pending records");
        List<IEodTransferItem> list = transferItemRepository.getICRPendingReportRecs();
        List<EodTransferItem> itemList = new ArrayList<>();
        for (IEodTransferItem ti : list) {
            EodTransferItem item = new EodTransferItem();
            if (ti.getTransferItemId() != null) {
                item.setTransferItemId(ti.getTransferItemId());
            }
            item.setOriginalCSEAccountNumber(ti.getOriginalCSEAccountNumber() == null ? "" : ti.getOriginalCSEAccountNumber());
            item.setOriginalCSEAccountType(ti.getOriginalCSEAccountType() == null ? "" : ti.getOriginalCSEAccountType());
            item.setOriginalCSEAccountNumber(ti.getOriginalCSEAccountCheckDigit() == null ? "" : ti.getOriginalAdpSecurityNumber());
            item.setAdpAccountNumber(ti.getAdpAccountNumber() == null ? "" : ti.getAdpAccountNumber());
            item.setAdpAccountType(ti.getAdpAccountType() == null ? "" : ti.getAdpAccountType());
            item.setAdpAccountCheckDigit(ti.getAdpAccountCheckDigit() == null ? "" : ti.getAdpAccountCheckDigit());
            item.setAdpSecurityNumber(ti.getAdpSecurityNumber() == null ? "       " : ti.getAdpSecurityNumber());
            item.setOriginalAdpSecurityNumber(ti.getOriginalAdpSecurityNumber() == null ? "       " : ti.getOriginalAdpSecurityNumber());
            item.setOrgSecurityDescr(ti.getOrgSecurityDescr() == null ? "" : ti.getOrgSecurityDescr());
            item.setOriginalCusIP(ti.getOriginalCusIP() == null ? "" : ti.getOriginalCusIP());
            item.setOriginalQuantity(ti.getOriginalQuantity());
            item.setQuantity(ti.getOriginalQuantity());
            item.setTransferredDate(null);
            item.setConfirmationReceivedDate(null);
            item.setLastUpdatedDate(ti.getLastUpdatedDate());
            item.setLastUpdatedName(ti.getLastUpdatedName());
        }
        return itemList;
    }

    private String getReportRec(EodTransferItem item) {
        int cashEntryFlag = item.getCashEntryFlag();

        StringBuffer rec = new StringBuffer();
        rec.append(item.getCSEAccountNumber());               // WS-ROAD-BOX-BRCH + WS-ROAD-BOX-ACCT
        rec.append(item.getCSEAccountType());                 // WS-ROAD-BOX-ACCT-TYPE
        rec.append(item.getCSEAccountCheckDigit());           // WS-ROAD-BOX-ACCT-CHK-NBR
        rec.append(item.getAdpAccountNumber());               // WS-ROAD-CUST-ACCT-BRCH & -ACCT
        rec.append(item.getAdpAccountType());                 // WS-ROAD-CUST-ACCT-TYPE
        rec.append(item.getAdpAccountCheckDigit());           // WS-ROAD-CUST-CHK-NBR
        rec.append(getQuantity(item.getOriginalQuantity()));  // WS-ROAD-SETTLEMT-DAY-QTY

        if (cashEntryFlag == 1) {
            rec.append(getQuantity(item.getOriginalQuantity()));  // WS-ROAD-SETTLEMT-DAY-QTY-NEW
        } else {
            rec.append(getQuantity(item.getQuantity()));          // WS-ROAD-SETTLEMT-DAY-QTY-NEW
        }

        rec.append(StringUtils.rightPad(item.getOriginalAdpSecurityNumber(), 7));  // WS-ROAD-SEC-ADP-NBR
        rec.append(StringUtils.rightPad(StringUtils.trimToEmpty(item.getOriginalCusIP()), 15)); // WS-ROAD-CUSIP
        rec.append(getDescription(formatString(item.getOrgSecurityDescr()))); // WS-ROAD-SEC-ADP-DESC1 - 6
        rec.append(StringUtils.rightPad(item.getAdpSecurityNumber(), 7));     // WS-ROAD-SEC-ADP-NBR-NEW

        if (cashEntryFlag == 1) {
            rec.append(StringUtils.rightPad(StringUtils.trimToEmpty(item.getOriginalCusIP()), 15)); // WS-ROAD-CUSIP
            rec.append(getDescription(formatString(item.getOrgSecurityDescr()))); // WS-ROAD-SEC-ADP-DESC1 - 6
        } else {
            rec.append(StringUtils.rightPad(StringUtils.trimToEmpty(item.getCusIP()), 15)); // WS-ROAD-CUSIP-NEW
            rec.append(getDescription(formatString(item.getSecurityDescr()))); // WS-ROAD-SEC-ADP-DESC1-NEW - 6
        }

        rec.append(getDate(item.getTransferredDate()));                    // WS-ROAD-TRANSFER-DATE
        rec.append(getDate(item.getConfirmationReceivedDate()));           // WS-ROAD-CONFIRM-DATE
        rec.append(getDate(item.getLastUpdatedDate()));                    // WS-ROAD-LAST-MODIFIED-DATE
        rec.append(getFixedSizeString(item.getLastUpdatedName(), 10));     // WS-ROAD-LAST-MODIFIED-BY

        return rec.toString();
    }
    //format date into yyyy-mm-dd format
    private String getDate(Date date) {
        String aDate = "";
        if (date != null) {
            aDate = fmt.format(date);
        }
        return StringUtils.rightPad(aDate, 10);
    }

    //set discription upto 180 characters as per the report specification
    private String getDescription(String descr) {
        return StringUtils.rightPad(descr.trim(), 180);
    }

    //remove new lines and tabs from the descriptions
    private String formatString(String val) {
        String str = val;
        if (!Utility.isEmpty(str)) {
            str = val.replaceAll("\n", " ");
            str = str.replaceAll("\r", "");
        }
        return str;
    }

    //quantity should be 17 characters in the report
    private String getQuantity(BigDecimal quantity) {
        String qty = "0.000000";
        String dec = "0";
        String num = "0";
        if (quantity != null) {
            qty = quantity.toString();
        }
        int loc = qty.indexOf('.');
        if (loc > 0) {
            num = qty.substring(0, loc);
            dec = qty.substring(loc + 1, loc + 6);
        }
        return StringUtils.leftPad(num + dec, 17, '0');
    }

    //function for getting String of specific size with padded spaces on right side.
    private String getFixedSizeString(String str, int size) {
        String st = "";
        if (!Utility.isEmpty(str)) {
            if (str.length() > size) {
                st = str.substring(0, size);
            } else {
                st = str;
            }
        }
        return StringUtils.rightPad(st, size);

    }
}
