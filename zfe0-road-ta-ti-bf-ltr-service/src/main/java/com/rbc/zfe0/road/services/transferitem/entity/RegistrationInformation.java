package com.rbc.zfe0.road.services.transferitem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_RGSTR_INFO")
public class RegistrationInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RGSTR_KEY")
    private String registrationKey;

    @Column(name = "TX_ID")
    private String transactionId;

    @Column(name = "ADDR_BOX")
    private String addressBox;

    @Column(name = "TEL")
    private String telephone;

    @Column(name = "FAX")
    private String fax;

    @Column(name = "DAIN_FIRM_ACCT_NO")
    private String dainFirmAccountNumber;

    @Column(name = "ADP_ACCT_NO")
    private String adpAccountNumber;

    @Column(name = "ADP_ACCT_TP")
    private String adpAccountType;

    @Column(name = "ADP_ACCT_CHK_DIGIT")
    private String adpAccountCheckDigit;

    @Column(name = "RGSTR_NM")
    private String registrationName;

    @Column(name = "BOD_STRT_DT")
    private Date bodStartDate;

    @Column(name = "BOD_END_DT")
    private Date bodEndDate;

    @Column(name = "EOD_END_DT")
    private Date eodEndDate;

    @Column(name = "EOD_STRT_DT")
    private Date eodStartDate;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateUserName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDateTime;

    @Column(name = "EMAIL_ID")
    private String emailId;


}
