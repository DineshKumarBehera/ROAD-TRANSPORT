package com.rbc.zfe0.road.eod.persistence.entity;

import lombok.*;


import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "T_NOTE")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_ID")
    private Integer noteId;

    @Column(name = "TSFR_ITM_ID")
    private Integer transferItemId;

    @Column(name = "NOTE_TXT")
    private String noteText;

    @Column(name = "STS_CD")
    private String statusCode;

    @Column(name = "EVNT_CD")
    private String eventCode;

    @Column(name = "CREAT_DT")
    private Date createdDate;

    @Column(name = "LST_UPDT_USR_NM")
    private String lastUpdateName;

    @Column(name = "LST_UPDT_DT_TM")
    private Date lastUpdateDate;


}
