package com.rbc.zfe0.road.services.utils;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.enumtype.*;
import com.rbc.zfe0.road.services.dto.transfertype.TransferTypeDTO;
import com.rbc.zfe0.road.services.entity.TransferType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TransferTypeUtil {

    public static String formatAcctNbrTypeDgt(String acctNo, String acctType, String acctChkDgt) {
        String retVal = "";
        if (!StringUtils.isBlank(acctNo) &&
                (acctNo.trim().equalsIgnoreCase(Constants.C)
                        || acctNo.trim().equalsIgnoreCase(Constants.ORI_SHRT)
                        || acctNo.trim().equalsIgnoreCase(Constants.NEW_SHRT)
                        || acctNo.trim().equalsIgnoreCase(Constants.ROLLBACK)
                        || acctNo.trim().equalsIgnoreCase(Constants.CHOICE)
                        || acctNo.trim().equalsIgnoreCase(Constants.NA)
                        || acctNo.trim().equalsIgnoreCase(Constants.BATCH_ENTRY_USER_CHOICE_VALUE)
                        || acctNo.trim().equalsIgnoreCase(Constants.BATCH_ENTRY_USER_CHOICE_NOT_CLIENT_VALUE)
                )) {
            retVal = BKEntryType.valueOf(acctNo.trim()).toString();
            return retVal;
        }
        if (!StringUtils.isBlank(acctNo)) {
            retVal += StringUtils.leftPad(acctNo.trim(), 8, '0') + "-";
            retVal = retVal.substring(0, 3) + "-" +retVal.substring(3);
        }
        if (!StringUtils.isBlank(acctType)) {
            retVal += acctType + "-";
        }
        if (!StringUtils.isBlank(acctChkDgt)) {
            retVal += acctChkDgt;
        }
        return retVal;
    }

    /**
     * Returns the four character transfer type code using
     * the two character code and the value of the cash issues
     * and security issues checkbox.
     *
     * @return
     */
    public static String getTransferTypeCodeFromFormData(TransferTypeDTO tt) {
        String retVal = null;
        if (tt.getTwoCharCode() != null) {
            StringBuffer buff = new StringBuffer();
            buff.append(tt.getTwoCharCode());
            if (tt.getIsCashIssues() && tt.getIsSecurityIssues()) {
                buff.append(Constants.TRANSFER_TYPE_CODE_SUFFIX_CASH_AND_SECURITY);
            } else if (tt.getIsCashIssues()) {
                buff.append(Constants.TRANSFER_TYPE_CODE_SUFFIX_CASH);
            } else if (tt.getIsSecurityIssues()) {
                buff.append(Constants.TRANSFER_TYPE_CODE_SUFFIX_SECURITY);
            }
            retVal = buff.toString();
        }
        return retVal;
    }

    /**
     * Get the full transfer type name from the form.  This consists of the
     * description and the suffix denoting what issue types are involved.
     */
    public static String getTransferTypeNameFromFormData(TransferTypeDTO tt) {
        StringBuffer buff = new StringBuffer();
        buff.append(tt.getDescription());
        if (tt.getIsCashIssues() && tt.getIsSecurityIssues()) {
            buff.append(" Cash and Security");
        } else if (tt.getIsCashIssues()) {
            buff.append(" Cash");
        } else if (tt.getIsSecurityIssues()) {
            buff.append(" Security");
        }
        return buff.toString();
    }

    /**
     * Use the form checkboxes for cash issues and security issues to determine the value for
     * the TranCode column.  1 for cash only, 2 for security only, and 3 for both.
     */
    public static String getTransferTypeTranCodeFromFormData(TransferTypeDTO tt) {
        String retVal = "0";
        if (tt.getIsCashIssues() && tt.getIsSecurityIssues()) {
            retVal = "3";
        } else if (tt.getIsCashIssues()) {
            retVal = "1";
        } else if (tt.getIsSecurityIssues()) {
            retVal = "2";
        }
        return retVal;
    }

    public static Map<String, Boolean> getCashSecurityDataFromTranCode(TransferType tt) {
        Map<String, Boolean> map = new HashMap<>();
        if (tt.getTranCode().equalsIgnoreCase("0")) {
            map.put(Constants.TRANSFER_TYPE_CASH, false);
            map.put(Constants.TRANSFER_TYPE_SECURITY, false);
        } else if (tt.getTranCode().equalsIgnoreCase("1")) {
            map.put(Constants.TRANSFER_TYPE_CASH, true);
            map.put(Constants.TRANSFER_TYPE_SECURITY, false);
        } else if (tt.getTranCode().equalsIgnoreCase("2")) {
            map.put(Constants.TRANSFER_TYPE_CASH, false);
            map.put(Constants.TRANSFER_TYPE_SECURITY, true);
        } else if (tt.getTranCode().equalsIgnoreCase("3")) {
            map.put(Constants.TRANSFER_TYPE_CASH, true);
            map.put(Constants.TRANSFER_TYPE_SECURITY, true);
        }
        return map;

    }
    public static String setBatchEntryTypeClose(String formEntryType) {
        String batchEntryType = null;
        if (formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_1)
                || formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_2)
                || formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_3)
                || formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_3)
                || formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_4)
                || formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_5)
                || formEntryType.equalsIgnoreCase(Constants.NORMAL_CLOSE_7)) {
            batchEntryType = Constants.CLOSE_TYPE_NORMAL;
        } else if (formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_1)
                || formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_2)
                || formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_3)
                || formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_4)
                || formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_5)
                || formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_6)
                || formEntryType.equalsIgnoreCase(Constants.ESCHEATED_CLOSE_7)) {
            batchEntryType = Constants.CLOSE_TYPE_ESCHEATED;
        } else if (formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_1)
                || formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_2)
                || formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_3)
                || formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_4)
                || formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_5)
                || formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_6)
                || formEntryType.equalsIgnoreCase(Constants.WORTHLESS_CLOSE_7)) {
            batchEntryType = Constants.CLOSE_TYPE_WORTHLESS;
        } else if (formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_1)
                || formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_2)
                || formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_3)
                || formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_4)
                || formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_5)
                || formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_6)
                || formEntryType.equalsIgnoreCase(Constants.REJECTED_CLOSE_7)) {
            batchEntryType = Constants.CLOSE_TYPE_ESCHEATED;
        } else if (formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_1)
                || formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_2)
                || formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_3)
                || formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_4)
                || formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_5)
                || formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_6)
                || formEntryType.equalsIgnoreCase(Constants.CONFISCATED_CLOSE_7)) {
            batchEntryType = Constants.CLOSE_TYPE_CONFISCATED;
        }
        return batchEntryType;
    }

    public static Integer setBox97EntryFlagForOTT(String formEntryType) {
        Integer box97EntryFlag = null;
        if (formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_1)
                || formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_2)
                || formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_3)
                || formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_4)
                || formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_5)
                || formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_6)
                || formEntryType.equalsIgnoreCase(Constants.OTT_ENTRY_7)) {
            box97EntryFlag = 0;
        } else if (formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_1)
                || formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_2)
                || formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_3)
                || formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_4)
                || formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_5)
                || formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_6)
                || formEntryType.equalsIgnoreCase(Constants.INTO_97_ENTRY_7)) {
            box97EntryFlag = 1;
        }
        return box97EntryFlag;
    }

    public static Map<String, Object> extractRecordTypeAndDebitCredit(String memoType) {
        Map<String, Object> memoTypeMap = new HashMap<>();
        if (memoType != null && !memoType.trim().equals(Constants.HYPHEN) && !StringUtils.isBlank(memoType)) {
            String[] arr = memoType.split(Constants.HYPHEN, 2);
            memoTypeMap.put(Constants.DEBIT_CREDIT, arr[0].trim().equalsIgnoreCase("") ? null : DebitCreditType.valueOf(arr[0].trim()));
            memoTypeMap.put(Constants.RECORD_TYPE, arr[1].trim().equalsIgnoreCase("") ? null : RecordType.valueOf(arr[1].trim()));
            return memoTypeMap;
        } else {
            return null;
        }
    }

    public static String generateMemoType(DebitCreditType debitOrCredit, RecordType recordType) {
        String retVal = null;
        if (debitOrCredit == null && recordType == null) {
            retVal = "-";
        }
        if (debitOrCredit != null) {
            retVal = debitOrCredit.name() + "-";
        } else {
            retVal = "-";
        }
        if (recordType != null) {
            retVal += recordType.name();
        }
        return retVal;
    }


    public static Boolean convertIntToBoolean(Integer value) {
        if (value != null) {
            return value == 1;
        } else {
            return false;
        }
    }

    public static Integer convertBooleanToInt(Boolean value) {
        if (value != null && value) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Map<String, CodeType> extractSharesFromCode(String code) {
        Map<String, CodeType> shareMap = new HashMap<>();
        if (!code.trim().equalsIgnoreCase("SH--CA--SEC-") && !StringUtils.isBlank(code)) {
            String[] arr = code.split(Constants.HYPHEN, 6);
            shareMap.put(arr[0], arr[1].trim().equals("") ? null : CodeType.valueOf(arr[1].trim()));
            shareMap.put(arr[2], arr[3].trim().equals("") ? null : CodeType.valueOf(arr[3].trim()));
            shareMap.put(arr[4], arr[5].trim().equals("") ? null : CodeType.valueOf(arr[5].trim()));
            return shareMap;
        } else {
            return null;
        }
    }

    public static String generateSharesFromCode(CodeType qty, CodeType dollars, CodeType security) {
        String retVal = null;
        if (qty != null) {
            retVal = "SH-" + qty;
        } else {
            retVal = "SH-";
        }
        if (dollars != null) {
            retVal += "-CA-" + dollars;
        } else {
            retVal += "-CA-";
        }
        if (security != null) {
            retVal += "-SEC-" + security;
        } else {
            retVal += "-SEC-";
        }
        return retVal;
    }
}
