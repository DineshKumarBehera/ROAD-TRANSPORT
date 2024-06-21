package com.rbc.zfe0.road.eod.persistence.dao;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.process.handler.mapper.BatchEntryMapper;
import com.rbc.zfe0.road.eod.process.handler.mapper.EntryAcctNbr;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodIntactItem;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.persistence.entity.BatchEntry;
import com.rbc.zfe0.road.eod.persistence.entity.TransferType;
import com.rbc.zfe0.road.eod.persistence.repository.BatchEntryRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferTypeRepository;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class BatchEntryDAO {

    private HashMap boxLocCodes = null;
    private List cashSecurityList = null;
    //private String box97Code = null;
    private String dummyAccount = null;
    private static final String CASH_CODE = "C";
    private static final String SECURITY_CODE = "S";
    private static final String BOX_97_DISPOSITION_CODE = "UU97";
    private static final String BOX_97_IN = "IN";
    private static final String BOX_97_OUT = "OUT";
    private static final String CONFIRMED_DISPOSITION_CODE = "CF00";
    private static final String USER_CHOICE = "_USR";
    private static final String LOOKUP_CODE = "_LKP";
    private static final String CLIENT_ACCOUNT_CODE = "C";
    private static final String NONCLIENT_ACCOUNT_CODE = "_UCN";
    private static final String SHARES_CHOICE_NA = "NA";
    private static final String SHARES_CHOICE_NEW_ISSUE = "NI";
    private static final String SHARES_CHOICE_ORIG_ISSUE = "OI";

    @Autowired
    BatchEntryRepository batchEntryRepository;

    @Autowired
    TransferTypeRepository transferTypeRepository;

    @Value("${rbc.road.dummy.dummy-account-branch}")
    private String dummyAccountBranch;
    @Value("${rbc.road.dummy.dummy-account-number}")
    private String dummyAccountNumber;
    @Value("${rbc.road.dummy.dummy-account-type}")
    private String dummyAccountType;
    @Value("${rbc.road.dummy.dummy-account-check-digit}")
    private String dummyAccountCheckDigit;
    @Value("${rbc.road.dummy.box-97-account-number}")
    private String box97Code;

    //get entry for Box location
    private HashMap createBoxLocationBatchEntry() {
        HashMap map = new HashMap();
        List boxLocEntries = getBoxLocationEntries(Constants.TRANSFER_TYPE_BOX_LOCATION);
        Iterator boxLocItr = boxLocEntries.iterator();
        BatchEntryMapper batchEntry = null;
        while (boxLocItr.hasNext()) {
            batchEntry = (BatchEntryMapper) boxLocItr.next();
            if (!Utility.isEmpty(batchEntry.getStatusCode()) &&
                    batchEntry.getStatusCode().equalsIgnoreCase(USER_CHOICE)) {
                map.put(batchEntry.getMemoAccountNumberNoTranslation().getEntryAcctNbr() +
                        batchEntry.getMemoAccountNumberNoTranslation().getEntryAcctType() +
                        batchEntry.getMemoAccountNumberNoTranslation().getEntryAcctCheckDigit(), batchEntry);
            }
        }
        return map;
    }

    private List convertBatchEntryToDTO(List<BatchEntry> batchEntries) {
        List<BatchEntryMapper> entriesDTO = new ArrayList<>();
        for (BatchEntry be : batchEntries) {
            BatchEntryMapper entry = new BatchEntryMapper();
            entry.setId(be.getBatchEntryId());
            entry.setTransferTypeCode(StringUtils.trimToNull(be.getTransferTypeCode()));
            entry.setStatusCode(StringUtils.trimToNull(be.getStatusCode()));
            entry.setIssueTypeCode(StringUtils.trimToNull(be.getIssueTypeCode()));
            entry.setBatchCode(StringUtils.trimToNull(be.getBatchCode()));
            entry.setEntryCode(StringUtils.trimToNull(be.getEntryCode()));
            entry.setEntryType(StringUtils.trimToNull(be.getEntryType()));
            entry.setMemoType(StringUtils.trimToNull(be.getMemoType()));
            String memoType = entry.getMemoType();
            String debitCredit = null;
            String recordType = null;
            EntryAcctNbr entryAcctNbr = new EntryAcctNbr();
            entryAcctNbr.setEntryAcctNbr(StringUtils.trimToNull(be.getMemoAccountNumber()));
            entryAcctNbr.setEntryAcctType(StringUtils.trimToNull(be.getMemoAccountType()));
            entryAcctNbr.setEntryAcctCheckDigit(StringUtils.trimToNull(be.getMemoAccountCheckDigit()));
            entry.setMemoAccountNumber(entryAcctNbr);

            translateMemoAccount(entry, entry.getMemoAccountNumberNoTranslation().getEntryAcctNbr());
            entry.setDummyAcct(be.getDummyAccountFlag() == 1);
            entry.setOriginalIssue(be.getOriginalIssueFlag() == 1);
            entry.setDbUpdate(be.getDbUpdateFlag() == 1);
            entry.setSharesFromCode(StringUtils.trimToNull(be.getSharesFromCode()));
            entry.setBox97Entry(be.getBox97EntryFlag() == 1);
            entry.setDeleteOneCsEntry(be.getDeleteOneCsEntryFlag() == 1);
            entry.setLastUpdateName(StringUtils.trimToNull(be.getLastUpdateName()));
            entry.setLastUpdateDate(be.getLastUpdateDt());
            entriesDTO.add(entry);
        }
        return entriesDTO;

    }

    /**
     * Translate any memo accounts that are "CLIENT", to "C".
     *
     * @param batchEntryIn
     * @param memoAccountIn
     */
    private void translateMemoAccount(BatchEntryMapper batchEntryIn, String memoAccountIn) {

        if (memoAccountIn != null && batchEntryIn.getMemoAccountNumberNoTranslation() != null) {
            if (Constants.BATCH_ENTRY_TYPE_CLIENT.equals(memoAccountIn)) {
                batchEntryIn.getMemoAccountNumberNoTranslation().setEntryAcctNbr(Constants.BATCH_ENTRY_TYPE_CLIENT_SHORT);
            }
        }
    }

    private List getCashSecurityTransferTypes() {
        List<TransferType> transferTypes = transferTypeRepository.getTransferTypes("3");
        for (TransferType tt : transferTypes) {
            cashSecurityList.add(tt.getTransferTypeCode());
        }
        return cashSecurityList;
    }

    /**
     * Entry method for this object
     *
     * @param eodItem
     * @param issueCount
     * @return list - list of eodTransferItems objects with Batch and Entry codes
     * @throws RoadException
     */
    public List getBatchEntryCodes(EodTransferItem eodItem, int issueCount) throws RoadException {
        List batchEntryCodes = null;

        if (eodItem.getStatusCode().equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
            batchEntryCodes = getOTTBatchEntryCodes(eodItem, issueCount);
        } else {
            batchEntryCodes = getCLOSEBatchEntryCode(eodItem, issueCount);
        }

        return batchEntryCodes;
    }

    public List getOTTBatchEntryCodes(EodTransferItem item, int issueCount) {
        ArrayList batchEntryCodes = null;
        ArrayList removeList = new ArrayList();
        String transferTypeCode = item.getTransferTypeCode();
        BatchEntryMapper batchEntry = null;
        batchEntryCodes = (ArrayList) getBatchEntryCodes(item, false);
        cashSecurityList = getCashSecurityTransferTypes();
        for (int count = 0; count < batchEntryCodes.size(); count++) {
            batchEntry = (BatchEntryMapper) batchEntryCodes.get(count);
            if (cashSecurityList.contains(transferTypeCode)) {
                if (!isValidCashSecurityBatchEntry(batchEntry.getIssueTypeCode(), item.getCashEntryFlag())) {
                    removeBatchEntryObj(removeList, batchEntry);
                }
            }
//remove Batch Entry codes for which the flag is set as true
            if ((issueCount != 1) && (batchEntry.isDeleteOneCsEntry())) {
                removeBatchEntryObj(removeList, batchEntry);
            }
            getSharesFromVal(item, batchEntry);
        }
        batchEntryCodes.removeAll(removeList);

        if (cashSecurityList.contains(transferTypeCode)) {
            batchEntryCodes.removeAll(removeBatchEntryForSecurity(batchEntryCodes, item));
        }

        return batchEntryCodes;
    }

    /**
     * Generates Batch and Entry codes for Closed Items
     *
     * @param item
     * @param issueCount - denotes number of issue for particular type(cash or security)
     * @return ArrayList - Issues with Batch and Entry codes
     */
    public ArrayList getCLOSEBatchEntryCode(EodTransferItem item, int issueCount) throws RoadException {
        ArrayList batchEntryCodes = null;
        ArrayList batchEntryList = new ArrayList();
        BatchEntryMapper batchEntry = null;
        BatchEntryMapper boxLocation = null;
        String dispositionCode = item.getDispositionCode();
        String cseAccountNumber = item.getCSEAccountNumber() +
                item.getCSEAccountType() + item.getCSEAccountCheckDigit();
        boolean addFlag = true;
        boolean isDummy = false;

        isDummy = checkDummy(item.getAdpAccountNumber() + item.getAdpAccountType() +
                item.getAdpAccountCheckDigit());
        batchEntryCodes = (ArrayList) getBatchEntryCodes(item, false);
        boxLocCodes = createBoxLocationBatchEntry();
        for (int count = 0; count < batchEntryCodes.size(); count++) {
            try {
                addFlag = true;
                batchEntry = ((BatchEntryMapper) batchEntryCodes.get(count));

                if (boxLocCodes.keySet().contains(cseAccountNumber)) {

                    if (batchEntry.getEntryCode().equalsIgnoreCase(LOOKUP_CODE) &&
                            (batchEntry.getBatchCode().equalsIgnoreCase(LOOKUP_CODE)) && !isDummy) {
                        boxLocation = ((BatchEntryMapper) boxLocCodes.get(cseAccountNumber));
                        if (boxLocation != null) {
                            batchEntry.setBatchCode(boxLocation.getBatchCode());
                            batchEntry.setEntryCode(boxLocation.getEntryCode());
//when Batch code and Entry codes are user choices then
//copy AdpAccountNumber for CLIENT otherwise copy BoxLocation
//as accountNumber for the Intact file. Please refer Batch and Entry codes
//requirement for details.
                            if (boxLocation.getMemoAccountNumber().getEntryAcctNbr().
                                    equalsIgnoreCase(CLIENT_ACCOUNT_CODE)) {
                                copyAccountNumber(batchEntry, item);
                            } else {
                                batchEntry.getMemoAccountNumber().setEntryAcctNbr(
                                        boxLocation.getMemoAccountNumber().getEntryAcctNbr());
                                batchEntry.getMemoAccountNumber().setEntryAcctType(
                                        boxLocation.getMemoAccountNumber().getEntryAcctType());
                                batchEntry.getMemoAccountNumber().setEntryAcctCheckDigit(
                                        boxLocation.getMemoAccountNumber().getEntryAcctCheckDigit());
                            }

                            batchEntry.setMemoType(boxLocation.getMemoType());
                        }
                    }
                    if (isCreditShortClient(item) && (item.getTransferTypeCode().equalsIgnoreCase("TFSE") ||
                            (item.getTransferTypeCode().equalsIgnoreCase("RTSE")))) {
                        addFlag = false;
                    }
                } else {
                    if (batchEntry.getEntryCode().equalsIgnoreCase(USER_CHOICE)) {
                        batchEntry.setEntryCode(item.getEntryCode());
                    }
                    if (batchEntry.getBatchCode().equalsIgnoreCase(USER_CHOICE)) {
                        batchEntry.setBatchCode(item.getBatchCode());
                    }
                }
//For non-dummy close item entry for batch as 3D  and Entry as DEL should not
//get repeated twise as it takes out shares for the origional issue
                if (!isDummy && dispositionCode.equalsIgnoreCase(Constants.CLOSE_TYPE_NORMAL)) {
                    if ((item.getCashEntryFlag() == 0) &&
                            isCreditShortClient(item) &&
                            (batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(NONCLIENT_ACCOUNT_CODE))
                            || (batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(USER_CHOICE))) {
                        addFlag = false;
                    } else if ((item.getCashEntryFlag() == 1) &&
                            (item.getEntryCode().equalsIgnoreCase("DEL")) &&
                            (!batchEntry.getBatchCode().startsWith("3"))) {
                        addFlag = false;
                    } else if ((item.getCashEntryFlag() == 1) &&
                            (!item.getEntryCode().equalsIgnoreCase("DEL")) &&
                            (batchEntry.getBatchCode().startsWith("3"))) {
                        addFlag = false;
                    } else if (!isCreditShortClient(item) &&
                            (item.getCashEntryFlag() == 0) &&
                            batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(CLIENT_ACCOUNT_CODE)) {
                        addFlag = false;
                    }

                } else if (isDummy && dispositionCode.equalsIgnoreCase(Constants.CLOSE_TYPE_NORMAL)) {
                    if (item.getCSEAccountNumber().trim().equalsIgnoreCase(CLIENT_ACCOUNT_CODE)) {
                        if (batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(USER_CHOICE)
                                || ((batchEntry.getMemoAccountNumber().getEntryAcctNbr().
                                equalsIgnoreCase(NONCLIENT_ACCOUNT_CODE)))) {
                            addFlag = false;
                        }
                    } else if (boxLocCodes.containsKey(cseAccountNumber)) {
                        if (!batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(USER_CHOICE)
                                && (!batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(NONCLIENT_ACCOUNT_CODE))) {
                            addFlag = false;
                        }
                    }

                    if (boxLocCodes.containsKey(cseAccountNumber)) {
                        copyAccountNumber(batchEntry, item);
                    }
                }
                if ((issueCount != 1) && (batchEntry.isDeleteOneCsEntry())) {
                    addFlag = false;
                }
                if (addFlag) {
                    batchEntryList.add(batchEntry);
                    getSharesFromVal(item, batchEntry);
                }
            } catch (Throwable t) {
                String batchEntryInfo = " NULL ";
                if (batchEntry != null) {
                    batchEntryInfo = "ID: " + batchEntry.getId() + ", Key: " + batchEntry.getKey();
                }
                throw new RoadException("Error encountered when retrieving batch entry codes,"
                        + " batchEntry " + batchEntryInfo, t);
            }
        }
//For CLNC and dummy copy boxlocation object when issue closes against box-location.
        if (isDummy && boxLocCodes.containsKey(cseAccountNumber) &&
                dispositionCode.equalsIgnoreCase(Constants.CLOSE_TYPE_NORMAL)) {
            batchEntry = ((BatchEntryMapper) boxLocCodes.get(cseAccountNumber));
            if (batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(CLIENT_ACCOUNT_CODE)) {
                copyAccountNumber(batchEntry, item);
            }
            getSharesFromVal(item, batchEntry);
            batchEntryList.add(batchEntry);
        }

        return batchEntryList;
    }

    private boolean isCreditShortClient(EodTransferItem item) {
        boolean status = false;

        if (CLIENT_ACCOUNT_CODE.equalsIgnoreCase(item.getCSEAccountNumber())) {
            status = true;
        }

        return status;
    }

    private boolean isValidCashSecurityBatchEntry(String issueType, int cashEntryFlg) {
        boolean valid = true;

        if (issueType.equalsIgnoreCase(SECURITY_CODE) && (cashEntryFlg == 1)) {
            valid = false;
        } else if (issueType.equalsIgnoreCase(CASH_CODE) && (cashEntryFlg == 0)) {
            valid = false;
        }

        return valid;
    }

    private void copyAccountNumber(BatchEntryMapper batchEntry, EodTransferItem item) {
        batchEntry.getMemoAccountNumber().setEntryAcctNbr(item.getAdpAccountNumber());
        batchEntry.getMemoAccountNumber().setEntryAcctType(item.getAdpAccountType());
        batchEntry.getMemoAccountNumber().setEntryAcctCheckDigit(item.getAdpAccountCheckDigit());
    }

    private void removeBatchEntryObj(List removeList, BatchEntryMapper batchEntryObj) {
        if (!removeList.contains(batchEntryObj)) {
            removeList.add(batchEntryObj);
        }
    }

    /*
     * This method decides on quantity and security number depends on the settings
     * in the database. Users will set Quantity, shares and security values from the UI.
     * The decision is about taking shares and security from the OriginalIssue or
     * newIssue.
     */
    private void getSharesFromVal(EodTransferItem item, BatchEntryMapper batchEntry) {
        BigDecimal orgQuantity = item.getOriginalQuantity();
        BigDecimal quantity = item.getQuantity();
        String orgQty = "0.0";
        String qty = "0.0";
        if (!Utility.isEmpty(orgQuantity)) {
            orgQty = orgQuantity.toString();
        }
        if (!Utility.isEmpty(quantity)) {
            qty = quantity.toString();
        }

        batchEntry.populateQtyDollarsSecurityFromSharesFromCode(batchEntry.getSharesFromCode());

        if (batchEntry.getQty().equalsIgnoreCase(SHARES_CHOICE_ORIG_ISSUE) &&
                (batchEntry.getDollars().equalsIgnoreCase(SHARES_CHOICE_NA) ||
                        (batchEntry.getDollars().equalsIgnoreCase(SHARES_CHOICE_NEW_ISSUE)))) {
            batchEntry.setQty(orgQty);
            batchEntry.setSecurity(item.getOriginalAdpSecurityNumber());
        } else if (batchEntry.getQty().equalsIgnoreCase(SHARES_CHOICE_NEW_ISSUE) &&
                ((batchEntry.getDollars().equalsIgnoreCase(SHARES_CHOICE_NA)) ||
                        (batchEntry.getDollars().equalsIgnoreCase(SHARES_CHOICE_NEW_ISSUE)))) {
            batchEntry.setQty(qty);
            batchEntry.setSecurity(item.getAdpSecurityNumber());
        } else if (batchEntry.getQty().equalsIgnoreCase(SHARES_CHOICE_NA) &&
                (batchEntry.getSecurity().equalsIgnoreCase(SHARES_CHOICE_NA))) {
            batchEntry.setQty("0.0"); //no security and shares for cash posting records
            batchEntry.setSecurity("");
        } else if ((batchEntry.getQty().equalsIgnoreCase(SHARES_CHOICE_NA) &&
                ((batchEntry.getDollars().equalsIgnoreCase(SHARES_CHOICE_NA)) ||
                        (batchEntry.getDollars().equalsIgnoreCase(SHARES_CHOICE_NEW_ISSUE))))) {
            batchEntry.setQty(qty);
            batchEntry.setSecurity(item.getAdpSecurityNumber());
        } else {
            log.error(" Combination  - " + batchEntry.getQty() + " -  " + batchEntry.getDollars()
                    + batchEntry.getSecurity() + "  not found for " + item.getTransferItemId());
            batchEntry.setQty("0.0");
            batchEntry.setSecurity(item.getOriginalAdpSecurityNumber());
        }
    }


    /**
     * This method removes entries which should not run twice as they
     * takes out shares from the original transfer item.
     *
     * @param batchEntryCodes
     * @param item
     * @return list of BatchEntryMapper objects to be removed.
     */
    private List removeBatchEntryForSecurity(List batchEntryCodes, EodTransferItem item) {
        Iterator itemItr = null;
        BatchEntryMapper batchEntry = null;
        List removeList = new ArrayList();
        String adpAccount = item.getAdpAccountNumber() + item.getAdpAccountType() +
                item.getAdpAccountCheckDigit();

        if (!Utility.isEmpty(batchEntryCodes)) {
            itemItr = batchEntryCodes.iterator();

            while (itemItr.hasNext()) {
                batchEntry = (BatchEntryMapper) itemItr.next();

                if (checkDummy(adpAccount) && (item.getCashEntryFlag() == 0)) {
//don't include Original Issue batch code entries for Cash and Securities
                    if (batchEntry.isOriginalIssue()) {
                        removeList.add(batchEntry);
                    }
                } else if ((!Utility.isEmpty(batchEntry.getBatchCode()) && (!Utility.isEmpty(batchEntry.getEntryCode())))
                        && ((batchEntry.getBatchCode().equalsIgnoreCase("3Y")) &&
                        (batchEntry.getEntryCode().equalsIgnoreCase("CGC")))) {
                    removeList.add(batchEntry);
                }

            }
        }

        return removeList;
    }

    //check account
    private boolean checkDummy(String adpAccountNumber) {
        dummyAccount = dummyAccountBranch + dummyAccountNumber + dummyAccountType + dummyAccountCheckDigit;
        return adpAccountNumber.equalsIgnoreCase(dummyAccount);
    }

    /**
     * We are using OriginalCSEAccountNumber to check whether transferItem was in UU97 location before close.
     * While closing the issue UI is updating CSEAccountNumber with user choice (client or box location).
     * UI code is copying origional UU97 status to OriginalCSEAccountNumber.
     * (Anyway EOD will delete this original transfer item as a part of clean process)
     *
     * @param statusCode - status code to checl Box97 in OTT
     * @param item       - TransferItem
     * @return boolean indicates about box97 transfer item
     */
    private boolean isBox97(String statusCode, EodTransferItem item) {
        boolean isBox97 = false;
        if (statusCode.equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
            isBox97 = item.getDispositionCode().equalsIgnoreCase(BOX_97_DISPOSITION_CODE);
        } else {
//for close - look for Box 97 in OriginalCSEAccountNumber.
            isBox97 = item.getOriginalCSEAccountNumber().equalsIgnoreCase(box97Code);
        }
        return isBox97;
    }

    private String getIssueType(EodTransferItem eodItem) {
        String type = CASH_CODE; //assuming DB  will always have CashEntryFlag set
        if (eodItem.getCashEntryFlag() == 0) {
            type = SECURITY_CODE;
        }
        return type;
    }

    /**
     * This method returns BatchEntryMapper objects for the criteria in the
     * given BatchEntryMapper such as transfer type code, dummy acct, box 97,
     * etc.
     * <p>
     * NOTE:  This is not part of an innder class.
     */
    public List getBatchEntryCodes(BatchEntryMapper batchEntry) {
        List entries = null;
        if (batchEntry.getStatusCode().equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
            entries = getOttBatchEntryCodes(batchEntry);
        } else {
            if (batchEntry.getEntryType().equalsIgnoreCase(Constants.CLOSE_TYPE_NORMAL)) {
                entries = getNormalCloseBatchEntryCodes(batchEntry);
            } else {
                entries = getNonNormalCloseBatchEntryCodes(batchEntry);
            }
        }
        return entries;
    }

    private List getBatchEntryCodes(EodTransferItem eodItem, boolean isBox97Entries) {
        List batchEntryCodes = null;
        String adpAccount = eodItem.getAdpAccountNumber() + eodItem.getAdpAccountType() +
                eodItem.getAdpAccountCheckDigit();
        BatchEntryMapper batchEntry = new BatchEntryMapper();
        batchEntry.setTransferTypeCode(eodItem.getTransferTypeCode());
        batchEntry.setStatusCode(eodItem.getStatusCode());
        batchEntry.setDummyAcct(checkDummy(adpAccount));
        if (isBox97Entries) {
            batchEntry.setBox97Entry(isBox97Entries);
        } else {
            batchEntry.setBox97Entry(isBox97(eodItem.getStatusCode(), eodItem));
        }
        batchEntry.setIssueTypeCode(this.getIssueType(eodItem));
        batchEntry.setEntryType(eodItem.getDispositionCode());
        batchEntryCodes = getBatchEntryCodes(batchEntry);
        return batchEntryCodes;
    }

    private List getOttBatchEntryCodes(BatchEntryMapper batchEntry) {
        List<BatchEntry> batchEntries = batchEntryRepository.getOttBatchEntryCodes(batchEntry.getTransferTypeCode(),
                batchEntry.getStatusCode(),
                batchEntry.getDummyAcct() ? 1 : 0,
                batchEntry.getBox97Entry() ? 1 : 0,
                batchEntry.getIssueTypeCode());
        return convertBatchEntryToDTO(batchEntries);
    }

    private List getNormalCloseBatchEntryCodes(BatchEntryMapper batchEntry) {
        List<BatchEntry> batchEntries = batchEntryRepository.getNormalCloseBatchEntryCodes(batchEntry.getTransferTypeCode(),
                batchEntry.getStatusCode(),
                batchEntry.getDummyAcct() ? 1 : 0,
                batchEntry.getBox97Entry() ? 1 : 0,
                batchEntry.getIssueTypeCode());
        return convertBatchEntryToDTO(batchEntries);
    }

    private List getNonNormalCloseBatchEntryCodes(BatchEntryMapper batchEntry) {
        List<BatchEntry> batchEntries = batchEntryRepository.getNonNormalCloseBatchEntryCodes(batchEntry.getTransferTypeCode(),
                batchEntry.getStatusCode(),
                batchEntry.getDummyAcct() ? 1 : 0,
                batchEntry.getBox97Entry() ? 1 : 0,
                batchEntry.getIssueTypeCode());
        return convertBatchEntryToDTO(batchEntries);
    }

    public List getUU97BatchEntryCodes(EodTransferItem eodItem) {
        List batchEntryCodes = null;
        batchEntryCodes = getOTTUU97BatchEntryCodes(eodItem);
        return batchEntryCodes;
    }

    /**
     * Only OTT can have UU97 disposition code.
     * This method decides retrieves batch and entry codes for transfertype having Disposition code as UU97.
     *
     * @param item
     * @return ArrayList - list of batch and entry codes
     */
    public ArrayList getOTTUU97BatchEntryCodes(EodTransferItem item) {
        ArrayList batchEntryCodes = null;
        ArrayList removeList = new ArrayList();
        String transferTypeCode = item.getTransferTypeCode();
        BatchEntryMapper batchEntry = null;
        batchEntryCodes = (ArrayList) getBatchEntryCodes(item, true);

        for (int count = 0; count < batchEntryCodes.size(); count++) {
            batchEntry = (BatchEntryMapper) batchEntryCodes.get(count);
//check for UU97
            if (getUU97Status(item).equalsIgnoreCase(BOX_97_OUT)) {
                if (batchEntry.getMemoType().equalsIgnoreCase(Constants.INTACT_REC_CREDIT_MEMO)) {
                    batchEntry.setMemoType(Constants.INTACT_REC_DEBIT_MEMO);
                } else {
                    batchEntry.setMemoType(Constants.INTACT_REC_CREDIT_MEMO);
                }
            }
            getSharesFromVal(item, batchEntry);
        }
        batchEntryCodes.removeAll(removeList);

        return batchEntryCodes;
    }

    //set indicator for UU97 disposition code transition
    private String getUU97Status(EodTransferItem item) {
        String uu97Status = "";

        if (item.getDispositionCode().equalsIgnoreCase(BOX_97_DISPOSITION_CODE)) {
            uu97Status = BOX_97_IN; //disposition code is UU97
        } else {
            uu97Status = BOX_97_OUT; //disposition code is not UU97 and item is confirmed
        }

        return uu97Status;
    }

    /*
     * Method getIntactItem
     * fills the transferagent object with values needed for printing intact and intact log file
     */
    public EodTransferItem getIntactItem(EodTransferItem item, BatchEntryMapper batchEntry
    ) {
        EodIntactItem eodItem = new EodIntactItem();

        eodItem.setAdpBranchCode(item.getAdpBranchCode());
        eodItem.setBatchCode(batchEntry.getBatchCode());
        eodItem.setEntryCode(batchEntry.getEntryCode());
        eodItem.setAdpAccountNumber(getAccountNumber(item, batchEntry));
        eodItem.setQuantity(new BigDecimal(getQuantity(item, batchEntry)));
        eodItem.setCheckAmount(item.getCheckAmount());
        eodItem.setOriginalAdpSecurityNumber(item
                .getOriginalAdpSecurityNumber().trim());
        eodItem.setAdpSecurityNumber(getSecurityNumber(item, batchEntry));
        eodItem.setSecurityDescr(Utility.formatString(item.getSecurityDescr()).trim());
        eodItem.setOrgSecurityDescr(Utility.formatString(item.getOrgSecurityDescr()));
        eodItem.setTransferTypeCode(item.getTransferTypeCode());
        eodItem.setStatusCode(item.getStatusCode().trim());
        eodItem.setCashEntryFlag(item.getCashEntryFlag());
        eodItem.setTransactionType(batchEntry.getMemoType());
        eodItem.setTransferredDate(item.getTransferredDate());
        eodItem.setCloseDate(item.getCloseDate());
        eodItem.setConfirmationReceivedDate(item.getConfirmationReceivedDate());
        eodItem.setLastUpdatedDate(item.getLastUpdatedDate());
        eodItem.setLastUpdatedName(item.getLastUpdatedName());
        eodItem.setOriginalCusIP(item.getOriginalCusIP());
        eodItem.setCusIP(item.getCusIP());
        eodItem.setControlId(item.getControlId());
        return eodItem;
    }

    //set proper account number for Intact file
    private String getAccountNumber(EodTransferItem item, BatchEntryMapper batchEntry) {
        String adpAccountNumber = "";
//get transfer item ADPAccount number for CLIENT and DUMMY
        if (batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(CLIENT_ACCOUNT_CODE) ||
                (batchEntry.getMemoAccountNumber().getEntryAcctNbr().equalsIgnoreCase(NONCLIENT_ACCOUNT_CODE))) {
            adpAccountNumber =
                    item.getAdpAccountNumber() + item.getAdpAccountType() + item.getAdpAccountCheckDigit();
        } else {
//replace account number as box location for Batch and Entry
            adpAccountNumber = batchEntry.getMemoAccountNumber().getEntryAcctNbr() +
                    batchEntry.getMemoAccountNumber().getEntryAcctType() +
                    batchEntry.getMemoAccountNumber().getEntryAcctCheckDigit();
        }

        return adpAccountNumber;
    }

    private String getSecurityNumber(EodTransferItem item, BatchEntryMapper obj) {
        String securityNumber = obj.getSecurity();

        if (obj.isOriginalIssue()) {
            securityNumber = item.getOriginalAdpSecurityNumber();
        }

        return securityNumber;
    }

    private String getQuantity(EodTransferItem item, BatchEntryMapper obj) {
        String quantity = "0.000000";

        if (!Utility.isEmpty(obj.getQty())) {
            quantity = obj.getQty();
        }

        if (obj.isOriginalIssue()) {
            if (!Utility.isEmpty(item.getOriginalQuantity())) {
                quantity = item.getOriginalQuantity().toString();
            }
        }

        return quantity;
    }

    private List getBoxLocationEntries(String transferTypeCode) {
        List<BatchEntry> boxLocEntries = batchEntryRepository.getBoxLocationEntries(Constants.TRANSFER_TYPE_BOX_LOCATION);
        return convertBatchEntryToDTO(boxLocEntries);
    }
}
