package com.rbc.zfe0.road.services.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Mapping TransferItem entity to T_ISSUE table in database.
 */

@Entity
@EqualsAndHashCode(exclude={"transferItem"})
@ToString(exclude = {"transferItem"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_ISS")
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ISS_ID")
    private Integer issueID;

//    @Column(name = "TRANSFERITEM_ID")
//    private Integer transferItemId;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "TRANSFERITEM_ID", referencedColumnName = "TRANSFERITEM_ID")
//    private TransferItem transferItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TSFR_ITM_ID")
    private TransferItem transferItem;

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

    @Column(name = "QTY")
    private BigDecimal quantity;

    @Column(name = "DAIN_SCTY_NO")
    private String dainSecurityNumber;

    @Column(name = "ADP_SCTY_NO")
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

    @Column(name = "CSE_ACCT_TP")
    private String cseAccountType;

    @Column(name = "CSE_ACCT_CHK_DIGIT_FLG")
    private String cseAccountCheckDigit;

    @Column(name = "CUSIP_NO")
    private String cusip;

    @Column(name = "SCTY_DESC")
    private String securityDescription;

    @Column(name = "BLK_FLG")
    private Boolean blockFlag;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

    @Column(name="CLS_FlG")
    private Integer closeFlag;
}