package com.rbc.zfe0.road.services.dto.transfertype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchEntryDTO implements Serializable {
    private Beottc beottc;
    private Beottd beottd;
    private Beclosc beclosc;
    private Beclosd beclosd;
    private Beclosc97 beclosc97;
    private Beclosd97 beclosd97;
}
