package com.rbc.zfe0.road.eod.process.handler.mapper;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class EodTransferItem {
    private int issueId;
    private int transferItemId;
    private String controlId;
    private Date closeDate;
    private String adpSecurityNumber;
    private String adpAccountType;
    private String adpAccountCheckDigit;
    private String adpBranchCode;
    private BigDecimal quantity;
    private BigDecimal originalQuantity;
    private String statusCode;
    private String dispositionCode;
    private Date dispositionDt;
    private String batchCode;
    private String entryCode;
    private Date lastUpdatedDate;
    private String lastUpdatedName;
    private Date confirmationSentDate;
    private Date confirmationReceivedDate;
    private Date transferredDate;
    private String originalCSEAccountNumber;
    private String originalCSEAccountType;
    private String originalCSEAccountCheckDigit;
    private String checkAmount;
    private String transferTypeCode;
    private String originalAdpSecurityNumber;
    private String adpAccountNumber;
    private String cusIP;
    private String OriginalCusIP;
    private int cashEntryFlag;
    private BigDecimal insuranceValue;
    private String CSEAccountNumber;
    private String CSEAccountType;
    private String CSEAccountCheckDigit;
    private String securityDescr;
    private String orgSecurityDescr;
    private BigDecimal transferAgentId;
    private String registrationTaxId;
    private String registrationDesc;
    private String certificateNumber;
    private String mailReceiptNumber;
    private String accountTaxId;
    private String note;
    private String repId;

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getAdpSecurityNumber() {
        return adpSecurityNumber;
    }

    public void setAdpSecurityNumber(String adpSecurityNumber) {
        this.adpSecurityNumber = adpSecurityNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public int getTransferItemId() {
        return transferItemId;
    }

    public void setTransferItemId(int transferItemId) {
        this.transferItemId = transferItemId;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getDispositionCode() {
        return dispositionCode;
    }

    public void setDispositionCode(String dispositionCode) {
        this.dispositionCode = dispositionCode;
    }

    public Date getDispositionDt() {
        return dispositionDt;
    }

    public void setDispositionDt(Date dispositionDt) {
        this.dispositionDt = dispositionDt;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getConfirmationReceivedDate() {
        return confirmationReceivedDate;
    }

    public void setConfirmationReceivedDate(Date confirmationReceivedDate) {
        this.confirmationReceivedDate = confirmationReceivedDate;
    }

    public Date getConfirmationSentDate() {
        return confirmationSentDate;
    }

    public void setConfirmationSentDate(Date confirmationSentDate) {
        this.confirmationSentDate = confirmationSentDate;
    }

    public Date getTransferredDate() {
        return transferredDate;
    }

    public void setTransferredDate(Date transferredDate) {
        this.transferredDate = transferredDate;
    }

    public String getOriginalCSEAccountNumber() {
        return originalCSEAccountNumber;
    }

    public void setOriginalCSEAccountNumber(String originalCSEAccountNumber) {
        this.originalCSEAccountNumber = originalCSEAccountNumber;
    }

    public String getAdpBranchCode() {
        return adpBranchCode;
    }

    public void setAdpBranchCode(String adpBranchCode) {
        this.adpBranchCode = adpBranchCode;
    }

    public String getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(String checkAmount) {
        this.checkAmount = checkAmount;
    }

    public String getTransferTypeCode() {
        return transferTypeCode;
    }

    public void setTransferTypeCode(String transferTypeCode) {
        this.transferTypeCode = transferTypeCode;
    }

    public String getOriginalAdpSecurityNumber() {
        return originalAdpSecurityNumber;
    }

    public void setOriginalAdpSecurityNumber(String originalAdpSecurityNumber) {
        this.originalAdpSecurityNumber = originalAdpSecurityNumber;
    }

    public String getAdpAccountNumber() {
        return adpAccountNumber;
    }

    public void setAdpAccountNumber(String adpAccountNumber) {
        this.adpAccountNumber = adpAccountNumber;
    }

    public String getOriginalCSEAccountCheckDigit() {
        return originalCSEAccountCheckDigit;
    }
    public void setOriginalCSEAccountCheckDigit(
            String originalCSEAccountCheckDigit) {
        this.originalCSEAccountCheckDigit = originalCSEAccountCheckDigit;
    }

    public String getOriginalCSEAccountType() {
        return originalCSEAccountType;
    }

    public void setOriginalCSEAccountType(String originalCSEAccountType) {
        this.originalCSEAccountType = originalCSEAccountType;
    }

    public String getAdpAccountType() {
        return adpAccountType;
    }

    public void setAdpAccountType(String adpAccountType) {
        this.adpAccountType = adpAccountType;
    }

    public String getAdpAccountCheckDigit() {
        return adpAccountCheckDigit;
    }

    public void setAdpAccountCheckDigit(String adpAccountCheckDigit) {
        this.adpAccountCheckDigit = adpAccountCheckDigit;
    }

    public int getCashEntryFlag() {
        return cashEntryFlag;
    }

    public void setCashEntryFlag(int cashEntryFlag) {
        this.cashEntryFlag = cashEntryFlag;
    }

    public String getCusIP() {
        return cusIP;
    }

    public void setCusIP(String cusIP) {
        this.cusIP = cusIP;
    }

    public BigDecimal getInsuranceValue() {
        return insuranceValue;
    }

    public void setInsuranceValue(BigDecimal insuranceValue) {
        this.insuranceValue = insuranceValue;
    }

    public BigDecimal getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(BigDecimal originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public String getOriginalCusIP() {
        return OriginalCusIP;
    }

    public void setOriginalCusIP(String originalcusIP) {
        OriginalCusIP = originalcusIP;
    }

    public String getLastUpdatedName() {
        return lastUpdatedName;
    }

    public void setLastUpdatedName(String lastUpdatedName) {
        this.lastUpdatedName = lastUpdatedName;
    }

    public String getCSEAccountCheckDigit() {
        return CSEAccountCheckDigit;
    }

    public void setCSEAccountCheckDigit(String accountCheckDigit) {
        CSEAccountCheckDigit = accountCheckDigit;
    }

    public String getCSEAccountNumber() {
        return CSEAccountNumber;
    }

    public void setCSEAccountNumber(String accountNumber) {
        CSEAccountNumber = accountNumber;
    }

    public String getCSEAccountType() {
        return CSEAccountType;
    }

    public void setCSEAccountType(String accountType) {
        CSEAccountType = accountType;
    }

    public String getOrgSecurityDescr() {
        return orgSecurityDescr;
    }

    public void setOrgSecurityDescr(String orgSecurityDesc) {
        this.orgSecurityDescr = orgSecurityDesc;
    }

    public String getSecurityDescr() {
        return securityDescr;
    }

    public void setSecurityDescr(String securityDesc) {
        this.securityDescr = securityDesc;
    }

    public BigDecimal getTransferAgentId() {
        return transferAgentId;
    }

    public void setTransferAgentId(BigDecimal transferAgentId) {
        this.transferAgentId = transferAgentId;
    }

    /**
     * @return Returns the registrationDesc.
     */
    public String getRegistrationDesc() {
        return registrationDesc;
    }
    /**
     * @param registrationDesc The registrationDesc to set.
     */
    public void setRegistrationDesc(String registrationDesc) {
        this.registrationDesc = registrationDesc;
    }
    /**
     * @return Returns the registrationTaxId.
     */
    public String getRegistrationTaxId() {
        return registrationTaxId;
    }
    /**
     * @param registrationTaxId The registrationTaxId to set.
     */
    public void setRegistrationTaxId(String registrationTaxId) {
        this.registrationTaxId = registrationTaxId;
    }
    /**
     * @return Returns the certificateNumber.
     */
    public String getCertificateNumber() {
        return certificateNumber;
    }
    /**
     * @param certificateNumber The certificateNumber to set.
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getMailReceiptNumber() {
        return mailReceiptNumber;
    }

    public void setMailReceiptNumber(String mailReceiptNumber) {
        this.mailReceiptNumber = mailReceiptNumber;
    }

    public String getAccountTaxId() {
        return accountTaxId;
    }

    public void setAccountTaxId(String accounttaxId) {
        this.accountTaxId = accounttaxId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRepId() {
        return repId;
    }

    public void setRepId(String repId) {
        this.repId = repId;
    }

    public boolean equalsICR(EodTransferItem re) {
        boolean match = false;
        try {
            //Logger.debug(getClass(), "CHECKING TI "
            //        + this.getTransferItemId() + " to " + re.getTransferItemId());

            if ((this.getCashEntryFlag() == 1) || (re.getCashEntryFlag() == 1)) {
                //Logger.debug(getClass(), "No match - cash entry");
                return false;
            }
            if (!this.getCSEAccountNumber().equals(re.getCSEAccountNumber())) {
                //Logger.debug(getClass(), "No match - CSE Account Number");
                return false;
            }
            if (!this.getCSEAccountType().equals(re.getCSEAccountType())) {
                //Logger.debug(getClass(), "No match - CSE Account Type");
                return false;
            }
            if (!this.getCSEAccountCheckDigit().equals(re.getCSEAccountCheckDigit())) {
                //Logger.debug(getClass(), "No match - CSE Account Check Digit");
                return false;
            }
            if (!this.getAdpAccountNumber().equals(re.getAdpAccountNumber())) {
                //Logger.debug(getClass(), "No match - ADP Account Check Number");
                return false;
            }
            if (!this.getAdpAccountType().equals(re.getAdpAccountType())) {
                //Logger.debug(getClass(), "No match - ADP Account Type");
                return false;
            }
            if (!this.getAdpAccountCheckDigit().equals(re.getAdpAccountCheckDigit())) {
                //Logger.debug(getClass(), "No match - ADP Check Digit");
                return false;
            }

            if (this.getOriginalAdpSecurityNumber().equals(re.getOriginalAdpSecurityNumber())
                    && this.getOriginalCusIP().equals(re.getOriginalCusIP())) {
                match = true;
            }

            String thisAdpSecNumber = (this.getAdpSecurityNumber() == null ? "" : this.getAdpSecurityNumber().trim());
            String thisCusip = (this.getCusIP() == null ? "" : this.getCusIP().trim());
            String reAdpSecNumber = (re.getAdpSecurityNumber() == null ? "" : re.getAdpSecurityNumber().trim());
            String reCusip = (re.getCusIP() == null ? "" : re.getCusIP().trim());

            if (!match && (thisAdpSecNumber.length() > 0) && (reAdpSecNumber.length() > 0)
                    && thisAdpSecNumber.equals(reAdpSecNumber)) {
                if ((thisCusip.length() > 0) && (reCusip.length() > 0) && thisCusip.equals(reCusip)) {
                    log.debug("CUSIP match");
                    match = true;
                } else if ((thisCusip.length() == 0) || (reCusip.length() == 0)) {
                    log.debug("CUSIP match on assumption");
                    match = true;
                }
            }
        } catch (Exception e) {
            log.error("Error matching transfer items "
                    + this.getTransferItemId() + " to " + re.getTransferItemId()
                    + ": " + e.getMessage());

        }
        log.debug("Returning match " + match + " for "
                + this.getTransferItemId() + " and " + re.getTransferItemId());
        return match;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }
    @Override
    public String toString() {
        return "EodTransferItem{" +
                "issueId=" + issueId +
                ", transferItemId=" + transferItemId +
                ", controlId='" + controlId + '\'' +
                ", closeDate=" + closeDate +
                ", adpSecurityNumber='" + adpSecurityNumber + '\'' +
                ", adpAccountType='" + adpAccountType + '\'' +
                ", adpAccountCheckDigit='" + adpAccountCheckDigit + '\'' +
                ", adpBranchCode='" + adpBranchCode + '\'' +
                ", quantity=" + quantity +
                ", originalQuantity=" + originalQuantity +
                ", statusCode='" + statusCode + '\'' +
                ", dispositionCode='" + dispositionCode + '\'' +
                ", dispositionDt=" + dispositionDt +
                ", batchCode='" + batchCode + '\'' +
                ", entryCode='" + entryCode + '\'' +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", lastUpdatedName='" + lastUpdatedName + '\'' +
                ", confirmationSentDate=" + confirmationSentDate +
                ", confirmationReceivedDate=" + confirmationReceivedDate +
                ", transferredDate=" + transferredDate +
                ", originalCSEAccountNumber='" + originalCSEAccountNumber + '\'' +
                ", originalCSEAccountType='" + originalCSEAccountType + '\'' +
                ", originalCSEAccountCheckDigit='" + originalCSEAccountCheckDigit + '\'' +
                ", checkAmount='" + checkAmount + '\'' +
                ", transferTypeCode='" + transferTypeCode + '\'' +
                ", originalAdpSecurityNumber='" + originalAdpSecurityNumber + '\'' +
                ", adpAccountNumber='" + adpAccountNumber + '\'' +
                ", cusIP='" + cusIP + '\'' +
                ", OriginalCusIP='" + OriginalCusIP + '\'' +
                ", cashEntryFlag=" + cashEntryFlag +
                ", insuranceValue=" + insuranceValue +
                ", CSEAccountNumber='" + CSEAccountNumber + '\'' +
                ", CSEAccountType='" + CSEAccountType + '\'' +
                ", CSEAccountCheckDigit='" + CSEAccountCheckDigit + '\'' +
                ", securityDescr='" + securityDescr + '\'' +
                ", orgSecurityDescr='" + orgSecurityDescr + '\'' +
                ", transferAgentId=" + transferAgentId +
                ", registrationTaxId='" + registrationTaxId + '\'' +
                ", registrationDesc='" + registrationDesc + '\'' +
                ", certificateNumber='" + certificateNumber + '\'' +
                ", mailReceiptNumber='" + mailReceiptNumber + '\'' +
                ", accountTaxId='" + accountTaxId + '\'' +
                ", note='" + note + '\'' +
                ", repId='" + repId + '\'' +
                '}';
    }
}