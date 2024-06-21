package com.rbc.zfe0.road.services.dto.transfertype;

import com.rbc.zfe0.road.services.dto.enumtype.CloseTypeCode;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cashbk implements Serializable {
    private Integer id;
    private Boolean isDelete = false;
    private String closeType;
    private String ottDebitLong;
    private String ottCreditShort;
    private String closedDebitLong;
    private String closedCreditShort;
}
