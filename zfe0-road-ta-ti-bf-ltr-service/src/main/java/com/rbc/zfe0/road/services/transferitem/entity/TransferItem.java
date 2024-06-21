package com.rbc.zfe0.road.services.transferitem.entity;


import com.rbc.zfe0.road.services.transferitem.model.HistoryNotes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_TSFR_ITM")
public class TransferItem implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TSFR_ITM_ID")
    private Integer transferItemId;

    @Column(name = "STS_CD")
    private String statusCode;


    @Column(name = "DAIN_FIRM_ACCT_NO")
    private String dainFirmAccountNumber;

    @Column(name = "ADP_ACCT_NO")
    private String adpAccountNumber;

    @Column(name = "ADP_ACCT_TP")
    private String adpAccountType;

    @Column(name = "ADP_ACCT_CHK_DIGIT")
    private String adpAccountCheckDigit;

    @Column(name = "DISPOS_CD")
    private String dispositionCode;

    @Column(name = "DISPOS_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date dispositionDt;

    @Column(name = "RCV_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date receivedDt;

    @Column(name = "TSFR_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date transferredDt;

    @Column(name = "RCV_CNFRM_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date confirmationReceivedDt;

    @Column(name = "SENT_CNFRM_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date confirmationSentDt;

    @Column(name = "ACCT_TX_ID")
    private String accountTaxId;

    @Column(name = "REP_ID")
    private String repId;

    @Column(name = "TSFR_EFF_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date transferEffectiveDt;

    @Column(name = "RGSTR_TX_ID")
    private String registrationTaxId;

    @Column(name = "RGSTR_DESC")
    private String registrationDescr;

    @Column(name = "MAIL_RCPT_NO")
    private String mailReceiptNumber;

    @Column(name = "ADP_BR_CD")
    private String adpBranchCode;

    @Column(name = "ALT_BR_CD")
    private String altBranchCode;

    @Column(name = "CLS_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date closeDt;

    @Column(name = "FIRM_ID")
    private String firmId;

    @Column(name = "ORGNL_SCTY_DESC")
    private String originalSecurityDescr;

    @Column(name = "ORGNL_DAIN_SEC_NO")
    private String originalDainSecurityNumber;

    @Column(name = "ORGNL_ADP_SEC_NO")
    private String originalAdpSecurityNumber;

    @Column(name = "CUSIP_NO")
    private String originalCusIp;
    @Column(name = "ORGNL_QTY")
    private BigDecimal originalQty;

    @Column(name = "ORGNL_DENOM_DESC")
    private String originalDenominatorDescr;

    @Column(name = "ORGNL_ENTR_CD")
    private String originalEntryCode;

    @Column(name = "ORGNL_ENTR_DT")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date originalEntryDt;

    @Column(name = "ORGNL_DL_ENTR_ACCT_NO")
    private String originalDleaAccountNumber;

    @Column(name = "ORGNL_DLE_ACCT_TYP")
    private String originalDleaAccountType;

    @Column(name = "ORGNL_DLE_ACCT_CHK_DIGIT")
    private String originalDleaAccountCheckDigit;

    @Column(name = "ORGNL_CSE_ACCT_NO")
    private String originalCseaAccountNumber;

    @Column(name = "ORGNL_CSE_ACCT_TP")
    private String originalCseaAccountType;

    @Column(name = "ORGNL_CSE_ACCT_CHK_DIGIT_FLG")
    private String originalCseaAccountCheckDigit;

    @Column(name = "BLK_FLG")
    private Integer blockFlag;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    @DateTimeFormat(pattern = "MM/DD/YYYY")
    private Date lastUpdateDt;

    @Column(name = "TLE_TSFR_TP")
    private String tleTransferType;
    @Column(name = "GFT_IND")
    private String giftIndicator;
    @Column(name = "CNTRL_ID")
    private String controlId;

    @Column(name = "LST_UPDT_USR_ROL")
    private String lastUpdateRole;

    @ManyToOne(targetEntity = DeliveryInstruction.class)
    @JoinColumn(name = "DELVRY_INSTRTN_ID", unique = true)
    private DeliveryInstruction deliveryInstruction;

    @ManyToOne(targetEntity = TransferAgent.class)
    @JoinColumn(name = "TSFR_AGT_ID", unique = true)
    private TransferAgent transferAgent;


    @OneToMany(mappedBy = "transferItem", cascade = CascadeType.ALL)
    private List<Issue> issue;


    @OneToMany(mappedBy = "transferItem", cascade = CascadeType.ALL)
    private List<Notes> notes;


    @ManyToOne(targetEntity = TransferType.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "TSFR_TP_CD", referencedColumnName = "TSFR_TP_CD")
    private TransferType transferType;

    public TransferItem clone() throws CloneNotSupportedException {
        return (TransferItem) super.clone();
    }
    public TransferItem cloneWithTransferItemIdNull() throws CloneNotSupportedException{
        TransferItem clonedTransferItem = clone();
        clonedTransferItem.setTransferItemId(null);
        clonedTransferItem.setIssue(null);
        clonedTransferItem.setNotes(null);
        return clonedTransferItem;
    }
}