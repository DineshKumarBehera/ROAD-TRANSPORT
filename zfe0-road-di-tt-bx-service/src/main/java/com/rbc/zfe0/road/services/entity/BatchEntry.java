package com.rbc.zfe0.road.services.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_BTCH_ENTR")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class BatchEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BTCH_ENTR_ID")
    private Integer batchEntryId;

    @Column(name="TSFR_TP_CD")
    private String transferTypeCode;

    @Column(name="STS_CD")
    private String statusCode;

    @Column(name="ISS_TP_CD")
    private String issueTypeCode;

    @Column(name="BTCH_CD")
    private String batchCode;

    @Column(name="ENTR_CD")
    private String entryCode;

    @Column(name="ENTR_TP")
    private String entryType;

    @Column(name="MEMO_TP")
    private String memoType;

    @Column(name="MEMO_ACCT_NO")
    private String memoAccountNumber;

    @Column(name="MEMO_ACCT_TP")
    private String memoAccountType;

    @Column(name="MEMO_ACCT_CHK_DIGIT_CD")
    private String memoAccountCheckDigit;

    @Column(name="DUMMY_ACCT_FLG_IND")
    private Integer dummyAccountFlag;

    @Column(name="ORGNL_ISS_FLG_IND")
    private Integer originalIssueFlag;

    @Column(name="DB_UPDT_FLG_IND")
    private Integer dbUpdateFlag;

    @Column(name="SHR_FROM_CD")
    private String sharesFromCode;

    @Column(name="BOX_97_ENTR_FLG")
    private Integer box97EntryFlag;

    @Column(name="DEL_ONE_SNTRY_FLG_IND")
    private Integer deleteOneCsEntryFlag;

    @Column(name="LST_UPDT_USR_NM")
    private String lastUpdateName;

    @UpdateTimestamp
    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;
}
