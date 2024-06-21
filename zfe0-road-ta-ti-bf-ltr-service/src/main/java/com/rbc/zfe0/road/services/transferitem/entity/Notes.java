package com.rbc.zfe0.road.services.transferitem.entity;

import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@EqualsAndHashCode(exclude = {"transferItem"})
@ToString(exclude = {"transferItem"})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "T_NOTE")
public class Notes implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTE_ID")
    private Integer noteId;
    @Column(name = "NOTE_TXT")
    private String notesTxt;
    @Column(name = "STS_CD")
    private String notesStsCode;
    @Column(name = "EVNT_CD")
    private String notesEventCode;
    @Column(name = "LST_UPDT_USR_NM")
    private String lastNoteUpdateName;
    @Column(name = "LST_UPDT_DT_TM")
    private Date lastNoteUpdateDt;

    @Column(name = "CREAT_DT")
    private Date createdDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TSFR_ITM_ID")
    private TransferItem transferItem;

    public Notes clone() throws CloneNotSupportedException {
        return (Notes) super.clone();
    }
    public Notes cloneWithNoteIdNull() throws CloneNotSupportedException {
        Notes clonedNotes = clone();
        clonedNotes.setNoteId(null);
        clonedNotes.setTransferItem(null);
        return clonedNotes;
    }
}
