package com.rbc.zfe0.road.services.transferitem.filter;

import com.rbc.zfe0.road.services.transferitem.utils.Constants;
import lombok.*;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferItemSearchFilter implements Serializable {
    private static final int MAX_QUICK_ACCT_LEN = 12;
    private static final String eq = " = ";
    private static final String like = " like ";
    private boolean eodProcess = false;
    private Boolean fromHistory = null;
    private int page = 0;
    private int pageSize = 100;
    private Integer totalAvailCount = null;
    private String advancedSearchInd = null;


    private List statusBeanList = new ArrayList();
    private List transferTypeBeanList = new ArrayList();
    private List closeTypeBeanList = new ArrayList();
    private List dispositionBeanList = new ArrayList();


    /** The Transfer Item Disposition, e.g. TX00, CF00, etc... */
    private String dispositionCode;


    /** Type of close, e.g. Normal, Worthless, etc... */
    private String closeType;


    MessageSourceAccessor messageSource;

    /** Transfer Item Status, e.g. Pending, Closed, etc... */
    private String status = Constants.STATUS_PENDING;

    /** Transfer Item Account Number */
    private String quickAccountNumber;
    private String accountNumber;

    /** Original or New Issue Security Number */
    private String origAdpSecurityNumber;
    private String adpSecurityNumber;
    private String oldDainSecurityNumber;
    private String quickSecurityNumber;

    /** Transfer Item Received Date */
    private RangeCriteriaDate receivedDate = new RangeCriteriaDate();
    private RangeCriteriaDate lastModifiedDate = new RangeCriteriaDate();
    private RangeCriteriaDate lastUpdateDt = new RangeCriteriaDate();

    private String transferAgent = null;
    private String lastModifiedBy = null;
    private String taxId = null;
    private String oldSecurityDesc = null;
    private String newSecurityDesc = null;
    private String oldCusip = null;
    private String newCusip = null;
    private String oldCreditShortEntryAccount = null;
    private String newCreditShortEntryAccount = null;
    private String oldDebitLongEntryAccount = null;
    private String newDebitLongEntryAccount = null;
    private BigDecimal oldQuantity = null;
    private BigDecimal newQuantity = null;
    private BigDecimal insuranceValue = null;



    /** Date Transfer Item was closed */
    private RangeCriteriaDate closedDate = new RangeCriteriaDate();
    private RangeCriteriaDate entryDate = new RangeCriteriaDate();

    /** Date Transfer Item was sent Out to Transfer */
    private RangeCriteriaDate transferredDate = new RangeCriteriaDate();
    /** Type of Transfer */
    private String transferTypeDesc;

    /** Date Confirmation was received from the Transfer Agent */
    private RangeCriteriaDate confirmationReceivedDate = new RangeCriteriaDate();

    /** Date Confirmation was sent from the Transfer Agent */
    private RangeCriteriaDate confirmationSentDate = new RangeCriteriaDate();


    private String sort = Constants.SORT_RECEIVED_DATE;
    private String sortDir = Constants.SORT_DIRECTION_DESC;

    private Integer itemId = null;
    private boolean guiSearch = false;

}