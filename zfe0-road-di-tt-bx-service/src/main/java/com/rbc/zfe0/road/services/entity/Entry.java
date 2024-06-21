package com.rbc.zfe0.road.services.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_ENTR")
public class Entry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENTR_ID")
    private Integer entryId;

    @Column(name = "TSFR_TP_CD")
    private String transferTypeCode;

    @Column(name = "ENTR_TP_CD")
    private String entryTypeCode;

    @Column(name = "OUT_TO_TSFR_LNG_OI_ACCT_NO")
    private String ottLongOiAccountNumber;

    @Column(name = "OUT_TO_TSFR_LNG_OI_ACCT_TP")
    private String ottLongOiAccountType;

    @Column(name = "OUT_TO_TSFR_LNG_OI_ACCT_CHK_DIGIT")
    private String ottLongOiAccountCheckDigit;

    @Column(name = "OUT_TO_TSFR_SHRT_OI_ACCT_NO")
    private String ottShortOiAccountNumber;

    @Column(name = "OUT_TO_TSFR_SHRT_IO_ACCT_TP")
    private String ottShortOiAccountType;

    @Column(name = "OUT_TO_TSFR_SHRT_OI_ACCT_CHK_DIGIT")
    private String ottShortOiAccountCheckDigit;

    @Column(name = "OUT_TO_TSFR_SHRT_OI_SRC_CD")
    private String ottOiSourceCode;

    @Column(name = "OUT_TO_TSFR_LNG_NI_ACCT_NO")
    private String ottLongNiAccountNumber;

    @Column(name = "OUT_TO_TSFR_LNG_NI_ACCT_TP")
    private String ottLongNiAccountType;

    @Column(name = "OUT_TO_TSFR_LNG_NI_ACCT_CHK_DIGIT")
    private String ottLongNiAccountCheckDigit;

    @Column(name = "OUT_TO_TSFR_SHRT_NI_ACCT_NO")
    private String ottShortNiAccountNumber;

    @Column(name = "OUT_TO_TSFR_SHRT_NI_ACCT_TP")
    private String ottShortNiAccountType;

    @Column(name = "OUT_TO_TSFR_SHRT_NI_ACCT_CHK_DIGIT")
    private String ottShortNiAccountCheckDigit;

    @Column(name = "OUT_TO_TSFR_SHRT_NI_SRC_CD")
    private String ottNiSourceCode;

    @Column(name = "CLS_LNG_OI_ACCT_NO")
    private String closedLongOiAccountNumber;

    @Column(name = "CLS_LNG_OI_ACCT_TP")
    private String closedLongOiAccountType;

    @Column(name = "CLS_LNG_OI_ACCT_CHK_DIGIT")
    private String closedLongOiAccountCheckDigit;

    @Column(name = "CLS_SHRT_OI_ACCT_NO")
    private String closedShortOiAccountNumber;

    @Column(name = "CLS_SHRT_IO_ACCT_TP")
    private String closedShortOiAccountType;

    @Column(name = "CLS_SHRT_OI_ACCT_CHK_DIGIT")
    private String closedShortOiAccountCheckDigit;

    @Column(name = "CLS_SHRT_OI_SRC_CD")
    private String closedOiSourceCode;

    @Column(name = "CLS_LNG_NI_ACCT_NO")
    private String closedLongNiAccountNumber;

    @Column(name = "CLS_LNG_NI_ACCT_TP")
    private String closedLongNiAccountType;

    @Column(name = "CLS_LNG_NI_ACCT_CHK_DIGIT")
    private String closedLongNiAccountCheckDigit;

    @Column(name = "CLS_SHRT_NI_ACCT_NO")
    private String closedShortNiAccountNumber;

    @Column(name = "CLS_SHRT_NI_ACCT_TP")
    private String closedShortNiAccountType;

    @Column(name = "CLS_SHRT_NI_ACCT_CHK_DIGIT")
    private String closedShortNiAccountCheckDigit;

    @Column(name = "CLS_SHRT_NI_SRC_CD")
    private String closedNiSourceCode;

    @Column(name = "CSH_ONLY_CD")
    private String cashOnlyCode;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

}
