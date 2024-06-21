package com.rbc.zfe0.road.services.transferitem.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Tim Tracy
 */
public class LabelValueBeans {

    public static List getSecurityChoices() {
        return LabelValueBeans.getQtyChoices();
    }

    public static List getDollarsChoices() {
        return LabelValueBeans.getQtyChoices();
    }

    public static List getQtyChoices() {
        List qtyChoiceList = new ArrayList();

        qtyChoiceList.add(LabelValueBean.SELECT);
        qtyChoiceList.add(LabelValueBean.NA);
        qtyChoiceList.add(new LabelValueBean(Constants.QTY_CHOICE_NI, Constants.QTY_CHOICE_NI));
        qtyChoiceList.add(new LabelValueBean(Constants.QTY_CHOICE_OI, Constants.QTY_CHOICE_OI));

        return qtyChoiceList;
    }


    public static List getBatchCodes() {
        List batchCodeList = new ArrayList();

        batchCodeList.add(LabelValueBean.SELECT);
        batchCodeList.add(LabelValueBean.LOOKUP);
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2D, Constants.BATCH_CODE_2D));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2G, Constants.BATCH_CODE_2G));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2H, Constants.BATCH_CODE_2H));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2J, Constants.BATCH_CODE_2J));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2Q, Constants.BATCH_CODE_2Q));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2P, Constants.BATCH_CODE_2P));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2R, Constants.BATCH_CODE_2R));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2U, Constants.BATCH_CODE_2U));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2W, Constants.BATCH_CODE_2W));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2Y, Constants.BATCH_CODE_2Y));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3D, Constants.BATCH_CODE_3D));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3P, Constants.BATCH_CODE_3P));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3Q, Constants.BATCH_CODE_3Q));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3R, Constants.BATCH_CODE_3R));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3U, Constants.BATCH_CODE_3U));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3W, Constants.BATCH_CODE_3W));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3Y, Constants.BATCH_CODE_3Y));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_CK, Constants.BATCH_CODE_CK));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_PH, Constants.BATCH_CODE_PH));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_TG, Constants.BATCH_CODE_TG));
        //Adding the two more batchCodes 2M and 3M start
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2M, Constants.BATCH_CODE_2M));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3M, Constants.BATCH_CODE_3M));
        //Adding the two more batchCodes 2M and 3M end

        //Adding the two more batchCodes 2I and 3I start
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_2I, Constants.BATCH_CODE_2I));
        batchCodeList.add(new LabelValueBean(Constants.BATCH_CODE_3I, Constants.BATCH_CODE_3I));
        //Adding the two more batchCodes 2I and 3I end

        return batchCodeList;
    }

    public static List getDebitCreditBeans() {
        List debitCreditList = new ArrayList();

        debitCreditList.add(LabelValueBean.SELECT);
        debitCreditList.add(new LabelValueBean(Constants.CHOICE_DEBIT, Constants.CHOICE_DEBIT));
        debitCreditList.add(new LabelValueBean(Constants.CHOICE_CREDIT, Constants.CHOICE_CREDIT));

        return debitCreditList;
    }

    public static List getRecordTypeBeans() {
        List recordTypeList = new ArrayList();

        recordTypeList.add(LabelValueBean.SELECT);
        recordTypeList.add(new LabelValueBean(Constants.RECORD_TYPE_MEMO, Constants.RECORD_TYPE_MEMO));
        recordTypeList.add(new LabelValueBean(Constants.RECORD_TYPE_STOCK_MONEY, Constants.RECORD_TYPE_STOCK_MONEY));
        recordTypeList.add(new LabelValueBean(Constants.RECORD_TYPE_CASH, Constants.RECORD_TYPE_CASH));

        return recordTypeList;
    }

    public static List getDispositionBeans() {
        List dispositionBeanList = new ArrayList();

        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_TX00, Constants.DISPOSITION_TX00));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_TX25, Constants.DISPOSITION_TX25));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_TX35, Constants.DISPOSITION_TX35));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_TX40, Constants.DISPOSITION_TX40));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_CF00, Constants.DISPOSITION_CF00));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_CF75, Constants.DISPOSITION_CF75));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_CF85, Constants.DISPOSITION_CF85));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_CF90, Constants.DISPOSITION_CF90));
        dispositionBeanList.add(new LabelValueBean(Constants.DISPOSITION_UU97, Constants.DISPOSITION_UU97));

        return dispositionBeanList;
    }


    public static List getTransferTypeBeans(Map transferTypeMapIn) {
        List transferTypeBeanList = new ArrayList();
        String transferTypeDesc = null;


        for (Iterator iter = transferTypeMapIn.keySet().iterator(); iter.hasNext(); ) {
            transferTypeDesc = (String) iter.next();

            transferTypeBeanList.add(
                    new LabelValueBean(transferTypeDesc));
        }

        Collections.sort(transferTypeBeanList);

        transferTypeBeanList.add(0, LabelValueBean.SELECT);

        return transferTypeBeanList;
    }

    /**
     * For Cash + Security transfer types, the user must choose if a batch entry
     * value is for Cash or Security
     *
     * @return
     */
    public static List getIssueTypes() {
        List retVal = new ArrayList();

        retVal.add(LabelValueBean.SELECT);
        retVal.add(new LabelValueBean("Cash", Constants.ISSUE_TYPE_CASH));
        retVal.add(new LabelValueBean("Security", Constants.ISSUE_TYPE_SECURITY));
        return retVal;
    }


    /**
     * Return a Map of closed short accts by close type code
     *
     * @return
     */
    public static List getManualAddShortAccts() {

        List cseEntries = new ArrayList();

        cseEntries.add(LabelValueBean.SELECT);
        cseEntries.add(new LabelValueBean("097-00023-1-7", "097-00023-1-7"));
        cseEntries.add(new LabelValueBean("000-00008-2-8", "000-00008-2-8"));

        return cseEntries;
    }

    /**
     * Return a Map of closed short accts by close type code
     *
     * @return
     */
    public static List getManualAddLongAccts() {
        List dleEntries = new ArrayList();

        dleEntries.add(LabelValueBean.SELECT);
        dleEntries.add(new LabelValueBean("Client", "C"));
        dleEntries.add(new LabelValueBean("C93", "C93"));
        dleEntries.add(new LabelValueBean("097-00012-1-0", "097-00012-1-0"));
        dleEntries.add(new LabelValueBean("097-00011-1-1", "097-00011-1-1"));
        dleEntries.add(new LabelValueBean("097-00023-1-7", "097-00023-1-7"));
        dleEntries.add(new LabelValueBean("097-00027-1-3", "097-00027-1-3"));
        dleEntries.add(new LabelValueBean("097-00028-1-2", "097-00028-1-2"));

        return dleEntries;
    }

    public static List getStateBeans() {
        List stateList = new ArrayList();

        stateList.add(LabelValueBean.SELECT);
        stateList.add(new LabelValueBean("ALABAMA", "AL"));
        stateList.add(new LabelValueBean("ALASKA", "AK"));
        stateList.add(new LabelValueBean("ARIZONA", "AZ"));
        stateList.add(new LabelValueBean("ARKANSAS", "AR"));
        stateList.add(new LabelValueBean("CALIFORNIA", "CA"));
        stateList.add(new LabelValueBean("COLORADO", "CO"));
        stateList.add(new LabelValueBean("CONNECTICUT", "CT"));
        stateList.add(new LabelValueBean("DELAWARE", "DE"));
        stateList.add(new LabelValueBean("DISTRICT OF COLUMBIA", "DC"));
        stateList.add(new LabelValueBean("FLORIDA", "FL"));
        stateList.add(new LabelValueBean("GEORGIA", "GA"));
        stateList.add(new LabelValueBean("HAWAII", "HI"));
        stateList.add(new LabelValueBean("IDAHO", "ID"));
        stateList.add(new LabelValueBean("ILLINOIS", "IL"));
        stateList.add(new LabelValueBean("INDIANA", "IN"));
        stateList.add(new LabelValueBean("IOWA", "IA"));
        stateList.add(new LabelValueBean("KANSAS", "KS"));
        stateList.add(new LabelValueBean("KENTUCKY", "KY"));
        stateList.add(new LabelValueBean("LOUISIANA", "LA"));
        stateList.add(new LabelValueBean("MAINE", "ME"));
        stateList.add(new LabelValueBean("MARYLAND", "MD"));
        stateList.add(new LabelValueBean("MASSACHUSETTS", "MA"));
        stateList.add(new LabelValueBean("MICHIGAN", "MI"));
        stateList.add(new LabelValueBean("MINNESOTA", "MN"));
        stateList.add(new LabelValueBean("MISSISSIPPI", "MS"));
        stateList.add(new LabelValueBean("MISSOURI", "MO"));
        stateList.add(new LabelValueBean("MONTANA", "MT"));
        stateList.add(new LabelValueBean("NEBRASKA", "NE"));
        stateList.add(new LabelValueBean("NEVADA", "NV"));
        stateList.add(new LabelValueBean("NEW HAMPSHIRE", "NH"));
        stateList.add(new LabelValueBean("NEW JERSEY", "NJ"));
        stateList.add(new LabelValueBean("NEW MEXICO", "NM"));
        stateList.add(new LabelValueBean("NEW YORK", "NY"));
        stateList.add(new LabelValueBean("NORTH CAROLINA", "NC"));
        stateList.add(new LabelValueBean("NORTH DAKOTA", "ND"));
        stateList.add(new LabelValueBean("OHIO", "OH"));
        stateList.add(new LabelValueBean("OKLAHOMA", "OK"));
        stateList.add(new LabelValueBean("OREGON", "OR"));
        stateList.add(new LabelValueBean("PENNSYLVANIA", "PA"));
        stateList.add(new LabelValueBean("RHODE ISLAND", "RI"));
        stateList.add(new LabelValueBean("SOUTH CAROLINA", "SC"));
        stateList.add(new LabelValueBean("SOUTH DAKOTA", "SD"));
        stateList.add(new LabelValueBean("TENNESSEE", "TN"));
        stateList.add(new LabelValueBean("TEXAS", "TX"));
        stateList.add(new LabelValueBean("UTAH", "UT"));
        stateList.add(new LabelValueBean("VERMONT", "VT"));
        stateList.add(new LabelValueBean("VIRGINIA", "VA"));
        stateList.add(new LabelValueBean("WASHINGTON", "WA"));
        stateList.add(new LabelValueBean("WEST VIRGINIA", "WV"));
        stateList.add(new LabelValueBean("WISCONSIN", "WI"));
        stateList.add(new LabelValueBean("WYOMING", "WY"));

        return stateList;
    }

    public static List getCloseTypeBeans() {
        return getCloseTypeBeans(false);
    }

    public static List getCloseTypeBeans(boolean includeOther) {
        List closedTypeList = new ArrayList();

        closedTypeList.add(LabelValueBean.SELECT);
        closedTypeList.add(new LabelValueBean("Normal", Constants.CLOSE_TYPE_NORMAL));
        closedTypeList.add(new LabelValueBean("Confiscated", Constants.CLOSE_TYPE_CONFISCATED));
        closedTypeList.add(new LabelValueBean("Escheated", Constants.CLOSE_TYPE_ESCHEATED));
        closedTypeList.add(new LabelValueBean("Rejected", Constants.CLOSE_TYPE_REJECTED));
        closedTypeList.add(new LabelValueBean("Worthless", Constants.CLOSE_TYPE_WORTHLESS));

        if (includeOther == true) {
            closedTypeList.add(new LabelValueBean("Other", Constants.CLOSE_TYPE_NOT_NORMAL));
        }

        return closedTypeList;
    }


    /**
     * @return
     */
    public static List getRejectionTypeBeans() {
        List rejectionTypeList = new ArrayList();

        rejectionTypeList.add(LabelValueBean.SELECT);

        String REJECTION_TYPE_CODE_INCORRECT_AGENT = "INCORRECT_AGENT";
        String REJECTION_TYPE_CODE_PAPERWORK_MISSING = "PAPERWORK_MISSING";
        String REJECTION_TYPE_CODE_RESETUP_REQUIRED = "RESETUP_REQUIRED";
        String REJECTION_TYPE_CODE_TRANSFER_FEES_MISSING = "TRANSFER_FEES_MISSING";
        String REJECTION_TYPE_CODE_OTHER = "OTHER";

        String REJECTION_TYPE_LBL_INCORRECT_AGENT = "Incorrect transfer agent";
        String REJECTION_TYPE_LBL_PAPERWORK_MISSING = "Paperwork missing";
        String REJECTION_TYPE_LBL_RESETUP_REQUIRED = "Re-setup required";
        String REJECTION_TYPE_LBL_TRANSFER_FEES_MISSING = "Transfer fees missing";
        String REJECTION_TYPE_LBL_OTHER = "Other";
        rejectionTypeList.add(new LabelValueBean(REJECTION_TYPE_LBL_INCORRECT_AGENT,
                REJECTION_TYPE_CODE_INCORRECT_AGENT));

        rejectionTypeList.add(new LabelValueBean(REJECTION_TYPE_LBL_PAPERWORK_MISSING,
                REJECTION_TYPE_CODE_PAPERWORK_MISSING));

        rejectionTypeList.add(new LabelValueBean(REJECTION_TYPE_LBL_RESETUP_REQUIRED,
                REJECTION_TYPE_CODE_RESETUP_REQUIRED));

        rejectionTypeList.add(new LabelValueBean(REJECTION_TYPE_LBL_TRANSFER_FEES_MISSING,
                REJECTION_TYPE_CODE_TRANSFER_FEES_MISSING));

        rejectionTypeList.add(new LabelValueBean(REJECTION_TYPE_LBL_OTHER,
                REJECTION_TYPE_CODE_OTHER));

        return rejectionTypeList;
    }

    // Translate the value back to the label
    public static String translateRejectionType(String rejectionTypeCode) {
        String retVal = "";
        List rejectionTypeLvBeans = LabelValueBeans.getRejectionTypeBeans();
        LabelValueBean lvBean = null;

        for (Iterator iter = rejectionTypeLvBeans.iterator(); iter.hasNext(); ) {
            lvBean = (LabelValueBean) iter.next();

            if (lvBean.getValue().equals(rejectionTypeCode)) {
                retVal = lvBean.getLabel();
                break;
            }

        }
        return retVal;
    }

    /**
     * @return
     */

}