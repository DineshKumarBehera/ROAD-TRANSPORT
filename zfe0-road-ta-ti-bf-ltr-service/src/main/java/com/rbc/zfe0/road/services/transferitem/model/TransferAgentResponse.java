package com.rbc.zfe0.road.services.transferitem.model;


import lombok.*;

import java.util.Date;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransferAgentResponse {
    private Integer transferAgentId;
    private String transferAgentName;
    private String addressBox;
    private String phoneNumber;
    private String faxNumber;
    private String feeAmount;
    private String lastUseDt;
    private String lastUpdateName;
    private Date lastUpdateDt;
    private Integer activeFlag;
    private String fee;
    private Integer tranCode;
    private Integer transferAgent_id;

}
