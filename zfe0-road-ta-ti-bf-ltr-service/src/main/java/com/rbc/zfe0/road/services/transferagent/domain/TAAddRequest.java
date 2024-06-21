package com.rbc.zfe0.road.services.transferagent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TAAddRequest {
    private String agentName;
    private String addressBox;
    private String phone;
    private String fax;
    private String fee;
    private String updatedBy;
    private String email;
}


