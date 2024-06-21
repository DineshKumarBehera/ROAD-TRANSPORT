package com.rbc.zfe0.road.services.transferagent.domain;

import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentDAO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name ="TAGenericResponse")
public class TAGenericResponse {
    boolean success = false;
    int recordCount = 0;
    String text = "";

    private TransferAgentDAO transferAgentDAO;

    public TAGenericResponse(boolean success) {
        this.success = success;
    }

    public TAGenericResponse(boolean success, int count) {
        this.success = success;
        recordCount = count;
    }

    public TAGenericResponse(boolean success, int count, String desc) {
        this.success = success;
        recordCount = count;
        text = desc;
    }

    public TAGenericResponse(boolean success, int count, TransferAgentDAO ta) {

        this.success = success;
        recordCount = count;
        transferAgentDAO = ta;
    }
}

