package com.rbc.zfe0.road.services.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_TSFR_TP")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
//@ToString(exclude = {"transferItem"})
@ToString
public class TransferType implements Serializable {

    @Id
    @Column(name="TSFR_TP_CD")
    private String transferTypeCode;

//    @OneToMany(mappedBy = "transferType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="TSFR_TP_CD")
//    private List<TransferItem> transferItem;

    @Column(name="TSFR_TP_NM")
    private String transferTypeName;

    @Column(name="UI_ACTV_FLG")
    private Integer uiActiveFlag;

    @Column(name="TXN_CD")
    private String tranCode;

    @Column(name="NM_OF_THE_USR_WHO_UPDT")
    private String lastUpdateName;

    @UpdateTimestamp
    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;

    @Column(name="AUTO_ROADBLOCK_CD_FLG")
    private Integer autoRoadBlockFlag;
}
