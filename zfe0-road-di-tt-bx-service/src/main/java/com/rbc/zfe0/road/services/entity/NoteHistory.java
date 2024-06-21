package com.rbc.zfe0.road.services.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "T_NOTE_HIST")
public class NoteHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_HIST_ID")
    private Integer noteHistoryId;

    @Column(name = "TSFR_ITM_HIST_ID")
    private Integer transferItemHistoryId;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "EVNT_CD")
    private String eventCode;

    @Column(name="STS_CD")
    private String statusCode;

    @Column(name="LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name="LST_UPDT_DT_TM")
    private Date lastUpdateDt;
}
