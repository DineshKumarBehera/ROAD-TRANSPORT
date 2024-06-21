package com.rbc.zfe0.road.services.transferagent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TADeleteRequest {
    private String agentId;
    private String idType;
    private String securityNumber;
}
