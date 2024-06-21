package com.rbc.zfe0.road.services.transferagent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class TAGetRequest {
    private String agentId;
    private String agentName;
    private List<Integer> agentIdsList;
    private String secNo;
}
