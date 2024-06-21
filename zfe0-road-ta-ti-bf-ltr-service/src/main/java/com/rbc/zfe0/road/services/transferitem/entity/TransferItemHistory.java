package com.rbc.zfe0.road.services.transferitem.entity;

import lombok.*;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "T_TSFR_ITM_HIST")
public class TransferItemHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TSFR_ITM_HIST_ID")
    private Integer transferItemHistoryId;

    @Column(name = "ITM_ID")
    private Integer transferItemId;

    @Column(name = "TSFR_TP_CD")
    private String transferTypeCode;

    @Column(name = "STS_CD")
    private String statusCode;

    @Column(name = "TSFR_AGT_NM")
    private String transferAgentName;

    @Column(name="TSFR_AGT_ADDR")
    private String transferAgentAddress;

    @Column(name="TSFR_AGT_FAX")
    private String transferAgentFax;

    @Column(name = "TSFR_AGT_ATTN_NM")
    private String transferAgentAttentionName;

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
    private Date dispositionDt;

    @Column(name = "RCV_DT")
    private Date receivedDt;

    @Column(name = "TSFR_DT")
    private Date transferredDt;

    @Column(name = "RCV_CNFRM_DT")
    private Date confirmationReceivedDt;

    @Column(name = "SENT_CNFRM_DT")
    private Date confirmationSentDt;

    @Column(name = "ACCt_TX_ID")
    private String accountTaxId;

    @Column(name = "REP_ID")
    private String repId;

    @Column(name = "TSFR_EFF_DT")
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
    private String originalDenominationDescr;

    @Column(name = "ORGNL_ENTR_CD")
    private String originalEntryCode;

    @Column(name = "ORGNL_ENTR_DT")
    private Date originalEntryDt;

    @Column(name = "ORGNL_DL_ENTR_ACCT_NO")
    private String originalDleAccountNumber;
    @Column(name = "ORGNL_DLE_ACCT_TP")
    private String originalDleAccountType;

    @Column(name = "ORGNL_DLE_ACCT_CHK_DIGIT")
    private String originalDleAccountCheckDigit;

    @Column(name = "ORGNL_CSE_ACCT_NO")
    private String originalCseAccountNumber;

    @Column(name = "ORGNL_CSE_ACCT_TP")
    private String originalCseAccountType;

    @Column(name = "ORGNL_CSE_ACCT_CHK_DIGIT_FLG")
    private String originalCseAccountCheckDigit;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

    @Column(name = "TLE_TSFR_TP")
    private String tleTransferType;

    @Column(name = "GFT_IND")
    private String giftIndicator;

    @Column(name = "CNTRL_ID")
    private String controlId;

    @Column(name = "ROL_OF_THE_PERS_WHO_UPDT_LST")
    private String lastUpdateRole;

    @OneToMany(mappedBy = "transferItemHistory", cascade = CascadeType.ALL)
    private List<NoteHistory> notesHistories;

    @OneToMany(mappedBy = "transferItemHistory", cascade = CascadeType.ALL)
    private List<IssueHistory> issueHistory;

    @Column(name = "DELVIERY_INSTRTN_NM")
    private String deliveryInstName;

    @Column(name = "DELVRY_INSTRTN_ADDR")
    private String deliveryInstAdd;

}