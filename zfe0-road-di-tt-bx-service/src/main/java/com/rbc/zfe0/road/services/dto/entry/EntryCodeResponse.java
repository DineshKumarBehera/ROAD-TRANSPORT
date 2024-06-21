package com.rbc.zfe0.road.services.dto.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EntryCodeResponse implements Serializable {

    private Integer entryCodeId;
    private String entryCodeName;
    private Boolean userChoice;
}