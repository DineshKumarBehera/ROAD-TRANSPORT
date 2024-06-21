package com.rbc.zfe0.road.services.transferitem.model;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryNotes {
    private int notId;
    private String lastUptTime;
    private String lastUpUsername;
    private String eventCode;
    private String stsCode;
    private String noteTxt;
}
