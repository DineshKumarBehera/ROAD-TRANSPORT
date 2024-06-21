package com.rbc.zfe0.road.services.dto.transfertype;

import com.rbc.zfe0.road.services.dto.enumtype.CloseTypeCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Secbk implements Serializable {
    private Integer id;
    private Boolean isDelete = false;
    private String closeType;
    private String ottDebitLong;
    private String ottCreditShort;
    private String closedDebitLong;
    private String closedCreditShort;
}
