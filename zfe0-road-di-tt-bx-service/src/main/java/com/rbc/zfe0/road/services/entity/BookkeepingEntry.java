package com.rbc.zfe0.road.services.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_BK_KPNG_ENTR")
public class BookkeepingEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BK_KPNG_ENTR_ID")
    private Integer bookkeepingEntryId;

    @Column(name = "TSFR_TP_CD")
    private String transferTypeCode;

    @Column(name = "ISS_TP_CD")
    private String issueTypeCode;

    @Column(name="CLS_TP_CD")
    private String closeTypeCode;

    @Column(name ="OUT_TO_TSFR_DL_ENTR_ACCT_NO" )
    private String ottDleAccountNumber;

    @Column(name = "OUT_TO_TSFR_DLE_ACCT_TP")
    private String ottDleAccountType;

    @Column(name = "OUT_TO_TSFR_DLE_ACCT_CHK_DIGIT")
    private String ottDleAccountCheckDigit;

    @Column(name = "OUT_TO_TSFR_CSE_ACCT_NO")
    private String ottCseAccountNumber;

    @Column(name = "OUT_TO_TSFR_CSE_ACCT_TP")
    private String ottCseAccountType;

    @Column(name = "OUT_TO_TSFR_CSE_ACCT_CHK_DIGIT_FLG")
    private String ottCseAccountCheckDigit;

    @Column(name = "CLS_DL_ENTR_ACCT_NO")
    private String closeDleAccountNumber;

    @Column(name = "CLS_DLE_ACCT_TP")
    private String closeDleAccountType;

    @Column(name = "CLS_DLE_ACCT_CHK_DIGIT")
    private String closeDleAccountCheckDigit;

    @Column(name = "CLS_CSE_ACCT_NO")
    private String closeCseAccountNumber;

    @Column(name = "CLS_CSE_ACCT_TP")
    private String closeCseAccountType;

    @Column(name = "CLS_CSE_ACCT_CHK_DIGIT_FLG")
    private String closeCseAccountCheckDigit;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @UpdateTimestamp
    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;
}
