package com.rbc.zfe0.road.services.transferitem.model;

import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferTypeResponse {
    private static final String TRANSFER_TYPE_LEGACY = "0";
    private static final String TRANSFER_TYPE_CASH_ONLY = "1";
    private static final String TRANSFER_TYPE_SECURITIES_ONLY = "2";
    private static final String TRANSFER_TYPE_CASH_AND_SECURITIES = "3";

    private String tranCode = null;
    private String transferTypeCode;
    private String transferTypeName;
    private Date lastUpdateDate;
    private Integer uiActiveFlag;
    private String lastUpdateName;
    private Integer autoRoadBlockFlag;

}
