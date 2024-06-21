package com.rbc.zfe0.road.services.transferitem.entity;

import lombok.*;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "T_NOTE_HIST")
public class NoteHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_HIST_ID")
    private Integer noteHistoryId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TSFR_ITM_HIST_ID")
    private TransferItemHistory transferItemHistory;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "EVNT_CD")
    private String eventCode;

    @Column(name = "STS_CD")
    private String statusCode;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDt;
}
