package com.rbc.zfe0.road.services.transferitem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewIssueSecurities {
    private Integer issueId;
    private String newSecurity;
    private String newCusip;
    private String newSecDesc;
    private BigDecimal newQuantity;
    private String newCart;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date newCartDate;
    private BigDecimal newInsurance;
    private String newDenomination;
    private String newDebitValue;
    private String newCredValue;
    private String newBatchCode;
    private String newEntryCode;
    private Date newEntryDate;
    private String certificateNumber;
    private String dleAccountNumber;
    private String cseAccountNumber;
    private Integer cashEntryFlag;
    private String lastUpdateName;
    private TransferTypeResponse transferTypeDetails;
}
