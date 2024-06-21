package com.rbc.zfe0.road.services.dto.transfertype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beottc implements Serializable {
    List<Entry> ottEntry;
    List<Entry> into97Entry;
}
