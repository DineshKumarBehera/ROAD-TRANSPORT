package com.rbc.zfe0.road.eod.dto;


import com.rbc.zfe0.road.eod.persistence.entity.Issue;

import java.math.BigDecimal;
import java.util.Date;

public interface ITransferItemExportSummary {

    Integer getTransferItemId();

    String getControlId();

    Date getCloseDate();

    String getStatusCode();

    String getCSEAccountNumber();

    Date getReceivedDt();

    String getCSEAccountType();

    String getCSEAccountCheckDigit();

    String getOriginalCseAccountNumber();

    String getOriginalCseAccountType();

    String getOriginalCseAccountCheckDigit();

    String getAdpAccountNumber();

    String getAdpAccountType();

    String getAdpAccountCheckDigit();

    String getAdpBranchCode();

    String getAltBranchCode();

    String getOriginalAdpSecurityNumber();

    String getOriginalCusIp();

    BigDecimal getOriginalQuantity();

    String getOriginalSecurityDescr();

    Date getTransferredDt();

    Date getLastUpdateDt();

    Date getConfirmationReceivedDt();

    Date getConfirmationSentDt();

    String getLastUpdateName();

    BigDecimal getQuantity();

    String getAdpSecurityNumber();

    String getDispositionCode();

    String getRegistrationTaxId();

    String getRegistrationDesc();

    String getMailReceiptNumber();

    String getAccountTaxId();

    String getRepId();

    String getTransferTypeCode();

    BigDecimal getInsuranceValue();

    String getCertificateNumber();

    Date getDispositionDt();

    String getEntryCode();

    String getSecurityDescr();

    Integer getCashEntryFlag();

    BigDecimal getCheckAmount();

    Date getCertificateDate();

    Issue getIssue();

    Integer getTransferAgentId();

    String getTransferAgentName();

    String getAddressBox();

    String getPhoneNumber();

    String getFaxNumber();

    String getFeeAmount();

    Date getLastUseDt();

    Integer getActiveFlag();

    Integer getDeliveryInstructionId();

    String getDeliveryInstructionName();

    void setDeliveryInstructionName(String deliveryInstructionName);
}

