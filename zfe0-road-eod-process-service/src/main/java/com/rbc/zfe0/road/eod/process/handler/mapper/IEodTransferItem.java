package com.rbc.zfe0.road.eod.process.handler.mapper;

import java.math.BigDecimal;
import java.util.Date;

public interface IEodTransferItem {
    Integer getTransferItemId();

    Integer getIssueId();

    String getControlId();

    Date getCloseDate();

    String getStatusCode();

    String getCSEAccountNumber();

    String getCSEAccountType();

    String getCSEAccountCheckDigit();

    String getOriginalCSEAccountNumber();

    String getOriginalCSEAccountType();

    String getOriginalCSEAccountCheckDigit();

    String getAdpAccountNumber();

    String getAdpAccountType();

    String getAdpAccountCheckDigit();

    String getAdpBranchCode();

    String getOriginalAdpSecurityNumber();

    String getOriginalCusIP();

    BigDecimal getOriginalQuantity();

    String getOrgSecurityDescr();

    Date getTransferredDate();

    Date getConfirmationSentDate();

    Date getConfirmationReceivedDate();

    Date getLastUpdatedDate();

    String getLastUpdatedName();

    BigDecimal getQuantity();

    String getAdpSecurityNumber();

    String getCusIP();

    String getSecurityDescr();

    Integer getCashEntryFlag();

    String getDispositionCode();

    Date getDispositionDt();

    String getEntryCode();

    Integer getTransferAgentId();

    String getRegistrationTaxId();

    String getRegistrationDesc();

    String getCertificateNumber();

    String getMailReceiptNumber();

    String getAccountTaxId();

    String getRepId();

    String getTransferTypeCode();

    BigDecimal getInsuranceValue();

    String getBatchCode();

    BigDecimal getCheckAmount();
}
