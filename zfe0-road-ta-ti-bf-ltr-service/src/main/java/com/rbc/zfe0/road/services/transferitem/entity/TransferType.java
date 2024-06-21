package com.rbc.zfe0.road.services.transferitem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "T_TSFR_TP")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class TransferType {

    @Id
    @Column(name="TSFR_TP_CD")
    private String transferTypeCode;

    @Column(name="TSFR_TP_NM")
    private String transferTypeName;

    @Column(name="UI_ACTV_FLG")
    private Integer uiActiveFlag;

    @Column(name="TXN_CD")
    private String tranCode;

    @Column(name="NM_OF_THE_USR_WHO_UPDT")
    private String lastUpdateName;

    @LastModifiedDate
    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

    @Column(name="AUTO_ROADBLOCK_CD_FLG")
    private Integer autoRoadBlockFlag;


}
