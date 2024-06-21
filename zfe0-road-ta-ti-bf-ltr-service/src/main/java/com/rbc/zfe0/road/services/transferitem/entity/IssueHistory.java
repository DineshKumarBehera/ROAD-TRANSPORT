package com.rbc.zfe0.road.services.transferitem.entity;

import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(exclude={"transferItemHistory"})
@ToString(exclude = {"transferItemHistory"})
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="T_ISS_HIST")
public class IssueHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITM_HIST_ID")
    private Integer issueHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TSFR_ITM_HIST_ID")
    private TransferItemHistory transferItemHistory;

    @Column(name = "BTCH_CD")
    private String batchCode;

    @Column(name = "CSH_ENTR_FLG")
    private Integer cashEntryFlag;

    @Column(name = "INS_VAL")
    private BigDecimal insuranceValue;

    @Column(name = "CHK_NO")
    private String checkNumber;

    @Column(name = "CHK_AMT")
    private BigDecimal checkAmount;

    @Column(name = "CERT_NO")
    private String certificateNumber;

    @Column(name = "CERT_DT")
    private Date certificateDate;

    @Column(name = "QTY")
    private BigDecimal quantity;

    @Column(name = "DAIN_SEC_NO")
    private String dainSecurityNumber;

    @Column(name = "ADP_SEC_NO")
    private String adpSecurityNumber;

    @Column(name = "DENOM")
    private String denomination;

    @Column(name = "ENTR_DT")
    private Date entryDate;

    @Column(name = "ENTR_CD")
    private String entryCode;

    @Column(name = "DL_ENTR_ACCT_NO")
    private String dleAccountNumber;

    @Column(name = "DLE_ACCT_TP")
    private String dleAccountType;

    @Column(name = "DLE_ACCT_CHK_DIGIT")
    private String dleAccountCheckDigit;

    @Column(name = "CSE_ACCT_NO")
    private String cseAccountNumber;

    @Column(name = "CSE_ACCT_TYP")
    private String cseAccountType;

    @Column(name = "CSE_ACCT_CHK_DIGIT_FLG")
    private String cseAccountCheckDigit;

    @Column(name = "CUSIP_NO")
    private String cusip;

    @Column(name = "SCTY_DESC")
    private String securityDescription;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;



}