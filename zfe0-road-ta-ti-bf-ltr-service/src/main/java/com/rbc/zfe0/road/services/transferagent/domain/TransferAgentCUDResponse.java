package com.rbc.zfe0.road.services.transferagent.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name="TransferAgentCUDResponse")

public class TransferAgentCUDResponse {
    boolean success = false;
    int recordCount = 0;
    private String agentId;
    private String message;

    public TransferAgentCUDResponse(boolean success, String agentId, String message)
    {
        if(success) recordCount = 1;
        this.success = success;
        this.agentId = agentId;
        this.message = message;
    }
}
