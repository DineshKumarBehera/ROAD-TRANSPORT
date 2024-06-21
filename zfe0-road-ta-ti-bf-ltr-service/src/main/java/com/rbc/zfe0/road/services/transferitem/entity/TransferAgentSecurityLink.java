package com.rbc.zfe0.road.services.transferitem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "T_TSFR_AGT_SCTY_LINK")
public class TransferAgentSecurityLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TSFR_AGT_SCTY_LINK_ID")
    private Integer transferAgentSecurityLinkId;

    @ManyToOne
    @JoinColumn(name = "AGT_ID")
    private TransferAgent transferAgent;

    @Column(name = "DAIN_SCTY_NO")
    private String dainSecurityNumber;

    @Column(name = "ADP_SCTY_NO")
    private String adpSecurityNumber;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

}
