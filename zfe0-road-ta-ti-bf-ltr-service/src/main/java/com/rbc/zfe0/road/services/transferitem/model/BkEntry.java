package com.rbc.zfe0.road.services.transferitem.model;

import com.rbc.zfe0.road.services.transferitem.dto.EntryAcctNbr;

import java.util.Date;

public class BkEntry {
    private boolean newEntry;
    private Integer id;
    private String transferTypeCode;
    private String issueTypeCode;
    private String closeTypeCode = null;


    private EntryAcctNbr closeCseNewIssueAcctNbr = null;
    private EntryAcctNbr closeDleNewIssueAcctNbr = null;

    private EntryAcctNbr ottCseNewIssueAcctNbr = null;
    private EntryAcctNbr ottDleNewIssueAcctNbr = null;

    // UI specific properties
    private String ottDleNewIssueAcctNbrSelected = null;
    private String ottCseNewIssueAcctNbrSelected = null;
    private String closeDleNewIssueAcctNbrSelected = null;
    private String closeCseNewIssueAcctNbrSelected = null;
    private boolean delete = false;
// End UI specific properties

    private String lastUpdateName;
    private Date lastUpdateDate;


    /**
     * @return Returns the closeCseEntryAcctNbrSelected.
     */
    public String getCloseCseNewIssueAcctNbrSelected() {
        return closeCseNewIssueAcctNbrSelected;
    }
    /**
     * @param closeCseEntryAcctNbrSelected The closeCseEntryAcctNbrSelected to set.
     */
    public void setCloseCseNewIssueAcctNbrSelected(
            String closeCseEntryAcctNbrSelected) {
        this.closeCseNewIssueAcctNbrSelected = closeCseEntryAcctNbrSelected;
    }
    /**
     * @return Returns the closeDleEntryAcctNbrSelected.
     */
    public String getCloseDleNewIssueAcctNbrSelected() {
        return closeDleNewIssueAcctNbrSelected;
    }
    /**
     * @param closeDleEntryAcctNbrSelected The closeDleEntryAcctNbrSelected to set.
     */
    public void setCloseDleNewIssueAcctNbrSelected(
            String closeDleEntryAcctNbrSelected) {
        this.closeDleNewIssueAcctNbrSelected = closeDleEntryAcctNbrSelected;
    }
    /**
     * @return Returns the ottCseEntryAcctNbrSelected.
     */
    public String getOttCseNewIssueAcctNbrSelected() {
        return ottCseNewIssueAcctNbrSelected;
    }
    /**
     * @param ottCseEntryAcctNbrSelected The ottCseEntryAcctNbrSelected to set.
     */
    public void setOttCseNewIssueAcctNbrSelected(String ottCseEntryAcctNbrSelected) {
        this.ottCseNewIssueAcctNbrSelected = ottCseEntryAcctNbrSelected;
    }
    /**
     * @return Returns the ottDleEntryAcctNbrSelected.
     */
    public String getOttDleNewIssueAcctNbrSelected() {
        return ottDleNewIssueAcctNbrSelected;
    }
    /**
     * @param ottDleEntryAcctNbrSelected The ottDleEntryAcctNbrSelected to set.
     */
    public void setOttDleNewIssueAcctNbrSelected(String ottDleEntryAcctNbrSelected) {
        this.ottDleNewIssueAcctNbrSelected = ottDleEntryAcctNbrSelected;
    }

    /**
     *
     */
    public BkEntry() {
        newEntry = true;
    }

    public BkEntry(Integer id) {
        this.id = id;
        newEntry = false;
    }

    public boolean isNew() {
        return newEntry;
    }

    public void convertToNew() {
        throw new UnsupportedOperationException("operation not supported");
    }

    public void setKey(Object key) {
        if ( !(key instanceof Integer) ) {
            throw new IllegalArgumentException("key must be of type Integer");
        }

        this.id = (Integer) key;
        this.newEntry = false;
    }

    public Object getKey() {
        return id;
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
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param idIn The id to set.
     */
    public void setId( Integer idIn ) {
        this.id = idIn;

        if( idIn != null )
        {
            this.newEntry = false;
        }
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
     * @return Returns the closedLongtNewIssueAcctNbr.
     */
    public EntryAcctNbr getCloseDleNewIssueAcctNbr() {
        return closeDleNewIssueAcctNbr;
    }
/**
 * @param closedLongtNewIssueAcctNbr The closedLongtNewIssueAcctNbr to set.
 */
public void setCloseDleNewIssueAcctNbr(
        EntryAcctNbr closedLongtNewIssueAcctNbr) {
    this.closeDleNewIssueAcctNbr = closedLongtNewIssueAcctNbr;
}
    /**
     * @return Returns the closedShortNewIssueAcctNbr.
     */
    public EntryAcctNbr getCloseCseNewIssueAcctNbr() {
        return closeCseNewIssueAcctNbr;
    }
    /**
     * @param closedShortNewIssueAcctNbr The closedShortNewIssueAcctNbr to set.
     */
    public void setCloseCseNewIssueAcctNbr(
            EntryAcctNbr closedShortNewIssueAcctNbr) {
        this.closeCseNewIssueAcctNbr = closedShortNewIssueAcctNbr;
    }

    /**
     * @return Returns the ottLongNewIssueAcctNbr.
     */
    public EntryAcctNbr getOttDleNewIssueAcctNbr() {
        return ottDleNewIssueAcctNbr;
    }

    /**
     * @param ottLongNewIssueAcctNbr The ottLongNewIssueAcctNbr to set.
     */
    public void setOttDleNewIssueAcctNbr(EntryAcctNbr ottLongNewIssueAcctNbr) {
        this.ottDleNewIssueAcctNbr = ottLongNewIssueAcctNbr;
    }

    /**
     * @return Returns the ottShortNewIssueAcctNbr.
     */
    public EntryAcctNbr getOttCseNewIssueAcctNbr() {
        return ottCseNewIssueAcctNbr;
    }
    /**
     * @param ottShortNewIssueAcctNbr The ottShortNewIssueAcctNbr to set.
     */
    public void setOttCseNewIssueAcctNbr(EntryAcctNbr ottShortNewIssueAcctNbr) {
        this.ottCseNewIssueAcctNbr = ottShortNewIssueAcctNbr;
    }

    /**
     * @return Returns the closeTypeCode.
     */
    public String getCloseTypeCode() {
        return closeTypeCode;
    }
    /**
     * @param closeTypeCode The closeTypeCode to set.
     */
    public void setCloseTypeCode(String closeTypeCode) {
        this.closeTypeCode = closeTypeCode;
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
}