package com.rbc.zfe0.road.services.entity;

import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_RGSTR_INFO")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class RegistrationInfo implements Serializable {

    @Id
    @Column(name = "RGSTR_KEY")
    private String registrationKey;

    @Column(name = "TX_ID")
    private String taxId;

    @Column(name = "ADDR_BOX")
    private String addressBox;

    @Column(name = "TEL")
    private String phoneNumber;

    @Column(name = "FAX")
    private String faxNumber;

    @Column(name = "DAIN_FIRM_ACCT_NO")
    private String dainFirmAccountNumber;

    @Column(name = "ADP_ACCT_NO")
    private String adpAccountNumber;

    @Column(name = "ADP_ACCT_TP")
    private String adpAccounttype;

    @Column(name = "ADP_ACCT_CHK_DIGIT")
    private String adpAccountCheckDigit;

    @Column(name = "RGSTR_NM")
    private String registrationName;

    @Column(name = "BOD_STRT_DT")
    private Date bodStartDt;

    @Column(name = "BOD_END_DT")
    private Date bodEndDt;

    @Column(name = "EOD_END_DT")
    private Date eodEndDt;

    @Column(name = "EOD_STRT_DT")
    private Date eodStartDt;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

}
