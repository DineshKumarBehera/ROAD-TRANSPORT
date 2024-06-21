package com.rbc.zfe0.road.eod.dto;

import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;

import java.math.BigDecimal;
import java.util.Date;

public interface IIssueExportSummary {
    Integer getIssueID();

    TransferItem getTransferItem();

    String getBatchCode();

    String getStatusCode();

    Integer getCashEntryFlag();

    BigDecimal getInsuranceValue();

    String getCheckNumber();

    BigDecimal getCheckAmount();

    String getCertificateNumber();

    Date getCertificateDate();

    BigDecimal getQuantity();

    String getDainSecurityNumber();

    String getAdpSecurityNumber();

    String getDenomination();

    Date getEntryDate();

    String getEntryCode();

    String getDleAccountNumber();

    String getDleAccountType();

    String getDleAccountCheckDigit();

    String getCseAccountNumber();

    String getCseAccountType();

    String getCseAccountCheckDigit();

    String getCusip();

    String getSecurityDescription();

    Boolean getBlockFlag();

    String getLastUpdateName();

    String getLastUpdateDt();

    Integer getCloseFlag();

    String getItemType();
}

