package com.rbc.zfe0.road.services.entity;

import lombok.*;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_TSFR_AGT")
public class TransferAgent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AGT_ID")
    private Integer transferAgentId;

    @Column(name = "AGT_NM")
    private String transferAgentName;

    @Column(name = "ADDR_BOX")
    private String addressBox;

    @Column(name = "TEL")
    private String phoneNumber;

    @Column(name = "FAX")
    private String faxNumber;

    @Column(name = "FEE_AMT")
    private String feeAmount;

    @Column(name = "LST_USED_DT")
    private Date lastUseDt;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

    @Column(name = "ACTV_FLG_IND")
    private Integer activeFlag;
}
