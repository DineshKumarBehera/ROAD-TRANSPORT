package com.rbc.zfe0.road.eod.process.handler.mapper;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.process.handler.mapper.EntryAcctNbr;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class BatchEntryMapper {
    private Integer id = null;
    private Boolean dummyAcct = Boolean.FALSE;
    private String statusCode = null;
    private String recordType = null;
    private String debitCredit = null;
    private String batchCode = null;
    private String entryType = null;
    private String acctBoxLoc = null;
    private String qty = null;
    private String dollars = null;
    private String security = null;
    private boolean newEntry = true;
    private String transferTypeCode = null;
    private String issueTypeCode = null;
    private String entryCode = null;
    private String memoType = null;
    private EntryAcctNbr memoAccountNumber = new EntryAcctNbr();
    private boolean originalIssue = false;
    private boolean dbUpdate = false;
    private String sharesFromCode = null;
    private Boolean box97Entry = Boolean.FALSE;
    private boolean deleteOneCsEntry = false;
    private String lastUpdateName = null;
    private Date lastUpdateDate = null;
    private boolean mapToClient = false;
    private boolean userChoice = false;

    private boolean delete = false;

    public BatchEntryMapper() {
        newEntry = true;
    }

    public BatchEntryMapper(Integer id) {
        this.id = id;
        newEntry = false;
    }

    public boolean isNew() {
        return newEntry;
    }

    /**
     * @return Returns the acctBoxLoc.
     */
    public String getAcctBoxLoc() {
        return acctBoxLoc;
    }

    /**
     * @param acctBoxLoc The acctBoxLoc to set.
     */
    public void setAcctBoxLoc(String acctBoxLoc) {
        this.acctBoxLoc = acctBoxLoc;
    }

    /**
     * @return Returns the batchCode.
     */
    public String getBatchCode() {
        return batchCode;
    }

    /**
     * @param batchCode The batchCode to set.
     */
    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    /**
     * @return Returns the debitCredit.
     */
    public String getDebitCredit() {
        return debitCredit;
    }

    /**
     * @param debitCredit The debitCredit to set.
     */
    public void setDebitCredit(String debitCredit) {
        this.debitCredit = debitCredit;
    }

    /**
     * @return Returns the dollars.
     */
    public String getDollars() {
        return dollars;
    }

    /**
     * @param dollars The dollars to set.
     */
    public void setDollars(String dollars) {
        this.dollars = dollars;
    }

    /**
     * @return Returns the dummyAcct.
     */
    public Boolean isDummyAcct() {
        return dummyAcct;
    }

    public Boolean getDummyAcct() {
        return dummyAcct;
    }

    /**
     * @param dummyAcct The dummyAcct to set.
     */
    public void setDummyAcct(Boolean dummyAcct) {
        this.dummyAcct = dummyAcct;
    }

    /**
     * @return Returns the entryCode.
     */
    public String getEntryType() {
        return entryType;
    }

    /**
     * @param entryTypeIn
     */
    public void setEntryType(String entryTypeIn) {
        this.entryType = entryTypeIn;
    }

    /**
     * @return Returns the qty.
     */
    public String getQty() {
        return qty;
    }

    /**
     * @param qty The qty to set.
     */
    public void setQty(String qty) {
        this.qty = qty;
    }

    /**
     * @return Returns the recordType.
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * @param recordType The recordType to set.
     */
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    /**
     * @return Returns the securityNumber.
     */
    public String getSecurity() {
        return security;
    }

    /**
     * @param securityNumber The securityNumber to set.
     */
    public void setSecurity(String securityNumber) {
        this.security = securityNumber;
    }

    /**
     * @return Returns the statusCode.
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The statusCode to set.
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return Returns the transferTypeCode.
     */
    public String getTransferTypeCode() {
        return transferTypeCode;
    }

    /**
     * @param transferTypeCode The transferTypeCode to set.
     */
    public void setTransferTypeCode(String transferTypeCode) {
        this.transferTypeCode = transferTypeCode;
    }

    /**
     * @return Returns the issueTypeCode.
     */
    public String getIssueTypeCode() {
        return issueTypeCode;
    }

    /**
     * @param issueTypeCode The issueTypeCode to set.
     */
    public void setIssueTypeCode(String issueTypeCode) {
        this.issueTypeCode = issueTypeCode;
    }

    /**
     * @return Returns the entryCode.
     */
    public String getEntryCode() {
        return entryCode;
    }

    /**
     * @param entryCode The entryCode to set.
     */
    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    /**
     * @return Returns the memoType.
     */
    public String getMemoType() {
        return memoType;
    }

    /**
     * @param memoType The memoType to set.
     */
    public void setMemoType(String memoType) {
        this.memoType = memoType;
    }

    public EntryAcctNbr getMemoAccountNumberNoTranslation() {
        return memoAccountNumber;
    }

    public void setMemoAccountNumberNoTranslation(EntryAcctNbr memoAccountNumber) {
        this.memoAccountNumber = memoAccountNumber;
    }
    /**
     * @return Returns the memoAccountNumber.
     */
    public EntryAcctNbr getMemoAccountNumber() {
// If this is a box location and the entry type is "C"...
        if (isBoxLocation() &&
                Constants.BATCH_ENTRY_TYPE_CLIENT_SHORT.equals(this.entryType)) {
// Return the client account number
            return new
                    EntryAcctNbr(Constants.BATCH_ENTRY_TYPE_CLIENT_SHORT);
        } else {
            return memoAccountNumber;
        }
    }

    public boolean isBoxLocation() {
        return Constants.TRANSFER_TYPE_BOX_LOCATION.equals(
                this.transferTypeCode);
    }

    // Populate whether or not this entry should be mapped to the client.  This is only valid
// for box locations.  The GUI has a checkbox that says "map to client", meaning entries
// will go against the client account.
    public void populateMapToClient(String entryTypeIn) {
        if (isBoxLocation() && Constants.BATCH_ENTRY_TYPE_CLIENT_SHORT.equals(this.entryType)) {
            this.mapToClient = true;
        } else {
            this.mapToClient = false;
        }
    }

    /**
     * @param memoAccountNumber The memoAccountNumber to set.
     */
    public void setMemoAccountNumber(EntryAcctNbr memoAccountNumber) {
        this.memoAccountNumber = memoAccountNumber;
    }

    /**
     * @return Returns the originalIssue.
     */
    public boolean isOriginalIssue() {
        return originalIssue;
    }

    /**
     * @param originalIssue The originalIssue to set.
     */
    public void setOriginalIssue(boolean originalIssue) {
        this.originalIssue = originalIssue;
    }

    /**
     * @return Returns the dbUpdate.
     */
    public boolean isDbUpdate() {
        return dbUpdate;
    }

    /**
     * @param dbUpdate The dbUpdate to set.
     */
    public void setDbUpdate(boolean dbUpdate) {
        this.dbUpdate = dbUpdate;
    }

    /**
     * @return Returns the box97Entry.
     */
    public Boolean isBox97Entry() {
        return box97Entry;
    }

    public Boolean getBox97Entry() {
        return box97Entry;
    }

    /**
     * @param box97Entry The box97Entry to set.
     */
    public void setBox97Entry(Boolean box97Entry) {
        this.box97Entry = box97Entry;
    }

    /**
     * @return Returns the deleteOneCsEntry.
     */
    public boolean isDeleteOneCsEntry() {
        return deleteOneCsEntry;
    }

    /**
     * @param deleteOneCsEntry The deleteOneCsEntry to set.
     */
    public void setDeleteOneCsEntry(boolean deleteOneCsEntry) {
        this.deleteOneCsEntry = deleteOneCsEntry;
    }

    /**
     * @return Returns the sharesFromCode.
     */
    public String getSharesFromCode() {
        return sharesFromCode;
    }

    /**
     * @param sharesFromCode The sharesFromCode to set.
     */
    public void setSharesFromCode(String sharesFromCode) {
        this.sharesFromCode = sharesFromCode;
    }

    public void setSharesFromCode(String qty, String dollars, String security) {
        StringBuffer buff = new StringBuffer();

        buff.append("SH-");
        buff.append(qty);
        buff.append("-CA-");
        buff.append(dollars);
        buff.append("-SEC-");
        buff.append(security);

        setSharesFromCode(buff.toString());
    }

    /**
     * Populte three fields from one column in the database.
     * <p>
     * Uses the code passed in to determine against what to generate entries for securities,
     * cash, and quantity.
     *
     * @param sharesFromCodeIn
     */
    public void populateQtyDollarsSecurityFromSharesFromCode(String sharesFromCodeIn) {
        String sharesFromCode = sharesFromCodeIn.toUpperCase();

        if (StringUtils.contains(sharesFromCode, "SH-NA")) {
            setQty(Constants.SHARES_CHOICE_NA);
        } else if (StringUtils.contains(sharesFromCode, "SH-NI")) {
            setQty(Constants.SHARES_CHOICE_NEW_ISSUE);
        } else if (StringUtils.contains(sharesFromCode, "SH-OI")) {
            setQty(Constants.SHARES_CHOICE_ORIG_ISSUE);
        }

// Set Cash
        if (StringUtils.contains(sharesFromCode, "CA-NA")) {
            setDollars(Constants.SHARES_CHOICE_NA);
        } else if (StringUtils.contains(sharesFromCode, "CA-NI")) {
            setDollars(Constants.SHARES_CHOICE_NEW_ISSUE);
        } else if (StringUtils.contains(sharesFromCode, "CA-OI")) {
            setDollars(Constants.SHARES_CHOICE_ORIG_ISSUE);
        }

// Set Security Nbr
        if (StringUtils.contains(sharesFromCode, "SEC-NA")) {
            setSecurity(Constants.SHARES_CHOICE_NA);
        } else if (StringUtils.contains(sharesFromCode, "SEC-NI")) {
            setSecurity(Constants.SHARES_CHOICE_NEW_ISSUE);
        } else if (StringUtils.contains(sharesFromCode, "SEC-OI")) {
            setSecurity(Constants.SHARES_CHOICE_ORIG_ISSUE);
        }

    }

    public void populateDebitCreditRecordTypeFromMemoTypeCode(String memoType) {

// We have one column in the database that maps to two different choices in the GUI.
// The RecordType could be "Memo", "Stock", or "Cash".  The DebitCredit could be
// either debit or credit
        if (memoType != null) {
            memoType = memoType.toLowerCase();

            if (StringUtils.contains(memoType, "credit")) {
                debitCredit = Constants.CHOICE_CREDIT;
            } else if (StringUtils.contains(memoType, "debit")) {
                debitCredit = Constants.CHOICE_DEBIT;
            }

            if (StringUtils.contains(memoType, "memo")) {
                recordType = Constants.RECORD_TYPE_MEMO;
            } else if (StringUtils.contains(memoType, "stock")) {
                recordType = Constants.RECORD_TYPE_STOCK_MONEY;
            } else if (StringUtils.contains(memoType, "cash")) {
                recordType = Constants.RECORD_TYPE_CASH;
            }
        }

        setRecordType(recordType);
        setDebitCredit(debitCredit);
    }

    public void setMemoType(String debitCredit, String recordType) {
        StringBuffer buff = new StringBuffer();

        buff.append(getDebitCredit());
        buff.append("-");

        if (Constants.RECORD_TYPE_MEMO.equals(getRecordType())) {
            buff.append(Constants.BATCH_ENTRY_MEMO_TYPE_MEMO);
        } else if (Constants.RECORD_TYPE_CASH.equals(getRecordType())) {
            buff.append(Constants.BATCH_ENTRY_MEMO_TYPE_CASH);
        } else if (Constants.RECORD_TYPE_STOCK_MONEY.equals(getRecordType())) {
            buff.append(Constants.BATCH_ENTRY_MEMO_TYPE_STOCK_MONEY);
        }

        setMemoType(buff.toString());

    }




    /**
     * @return Returns the lastUpdateDate.
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate The lastUpdateDate to set.
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * @return Returns the lastUpdateName.
     */
    public String getLastUpdateName() {
        return lastUpdateName;
    }

    /**
     * @param lastUpdateName The lastUpdateName to set.
     */
    public void setLastUpdateName(String lastUpdateName) {
        this.lastUpdateName = lastUpdateName;
    }

    public void setKey(Object key) {
        if (!(key instanceof Integer)) {
            throw new IllegalArgumentException("key must be of type Integer");
        }

        this.id = (Integer) key;
        this.newEntry = false;
    }

    /* (non-Javadoc)
     * @see com.rbcdain.road.domain.PersistentDomainObject#getKey()
     */
    public Object getKey() {
        return this.id;
    }

    public void convertToNew() {
        throw new UnsupportedOperationException("operation not supported");
    }


    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param idIn
     */
    public void setId(Integer idIn) {
        this.id = idIn;

        if (idIn != null) {
            this.newEntry = false;
        }
    }

    /**
     * @return Returns the delete.
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * @param delete The delete to set.
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    /**
     * If the GUI user has set the mapToClient flag...
     */
    public void setEntryTypeFromMapToClient() {
        if (this.isBoxLocation()) {
            if (this.isMapToClient()) {
// Change the entry type to client so we know to translate later
                this.entryType = Constants.BATCH_ENTRY_TYPE_CLIENT_SHORT;
            } else {
// Clear the entry type
                this.entryType = null;
            }
        }
    }

    /**
     * @return Returns the mapToClient.
     */
    public boolean isMapToClient() {
        return mapToClient;
    }

    /**
     * @param mapToClient The mapToClient to set.
     */
    public void setMapToClient(boolean mapToClient) {
        this.mapToClient = mapToClient;
    }

    /**
     * @return Returns the userChoice.
     */
    public boolean isUserChoice() {
        return userChoice;
    }

    /**
     * @param userChoice The userChoice to set.
     */
    public void setUserChoice(boolean userChoice) {
        this.userChoice = userChoice;
    }

    /**
     * Populate whether or not this entry should be available as a user choice.
     */
    public void populateUserChoice() {
        if (Constants.BATCH_ENTRY_USER_CHOICE_VALUE.equals(getStatusCode())) {
            this.setUserChoice(true);
        }

    }
}
