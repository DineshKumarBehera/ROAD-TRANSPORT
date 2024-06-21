package com.rbc.zfe0.road.services.transferitem.dto;


import com.rbc.zfe0.road.services.transferitem.filter.RangeCriteriaDate;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferItemResponse {
    private int transferItemId;
    private String itemRecd;
    private String itemRecdDateTo;
    private String transferDate;
    private String transferDateTo;
    private String closeDate;
    private String itemAcct;
    private String issueType;
    private String sec;
    private String originalSec;
    private String cusIp;
    private String originalCusIp;
    private BigDecimal itemQty;
    private BigDecimal originalQuantity;
    private String secDesc;
    private String originalSecDesc;
    private String registrationDescr;
    private BigDecimal itemValue;
    private String lastModified;
    private String lastModifiedTo;
    private String lastModifiedBy;
    private Date lastModifyDate;
    private String altBranchCode;
    private String branchCode;
    private String repId;
    private String acctTaxId;
    private String dispositionCode;
    private String transferType;
    private String transferAgentAttentionName;
    private int deliveryInstructionId;
    private String deliveryInstructionName;
    private String mailReceiptNumber;
    private String statusCode;
    private Date issueEntryDate;
    private String controlId;
    private String confirmationSentDt;
    private String confirmationReceiveDDt;
    private String confirmationReceiveDDtTo;
    private String dispositionDt;
    private int  issueId;
    private BigDecimal checkAmount;


    /** Transfer Item Received Date */
    private RangeCriteriaDate receivedDate = new RangeCriteriaDate();
    private RangeCriteriaDate lastModifiedDate = new RangeCriteriaDate();



    /** Date Transfer Item was closed */
    private RangeCriteriaDate closedDate = new RangeCriteriaDate();
    private RangeCriteriaDate entryDate = new RangeCriteriaDate();


    /** Date Transfer Item was sent Out to Transfer */
    private RangeCriteriaDate transferredDate = new RangeCriteriaDate();

    /** Date Confirmation was received from the Transfer Agent */
    private RangeCriteriaDate confirmationReceivedDate = new RangeCriteriaDate();

    /** Date Confirmation was sent from the Transfer Agent */
    private RangeCriteriaDate confirmationSentDate = new RangeCriteriaDate();
}
