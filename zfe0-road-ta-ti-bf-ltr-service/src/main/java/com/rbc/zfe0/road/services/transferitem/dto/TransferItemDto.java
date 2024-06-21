package com.rbc.zfe0.road.services.transferitem.dto;


import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TransferItemDto {
    private int transferItem;
    private String statusCode;
    private String accountNumber;
    private Date itemReceivedFrom;
    private Date itemReceivedTo;
    private Date transferredDateFrom;
    private Date transferredDateTo;
    private Date confirmedDateFrom;
    private Date confirmedDateTo;
    private Date lastModifiedFrom;
    private Date lastModifiedTo;
    private String lastModifiedBy;
    private String registrationDescription;
    private String accountTaxId;
    private String description;
    private String originalCusIp;
    private String longDesc;
    private BigDecimal originalQuantity;
    private BigDecimal newQuantity;
    private String dispositionCode;
    private String transferTypeDesc;
    private String transferAgent;
    private String insuranceValue;
    private String oldSecurityDesc;
    private String newSecurityDesc;
    private String oldCusip;
    private String newCusip;
    private String oldCreditShortEntryAccount;
    private String newCreditShortEntryAccount;
    private String oldDebitLongEntryAccount;
    private String newDebitLongEntryAccount;
    private String origAdpSecurityNumber;
    private String adpSecurityNumber;
    private String oldDainSecurityNumber;
}
