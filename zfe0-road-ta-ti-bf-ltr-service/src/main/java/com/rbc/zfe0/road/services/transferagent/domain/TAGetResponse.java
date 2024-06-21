package com.rbc.zfe0.road.services.transferagent.domain;

import com.rbc.zfe0.road.services.transferagent.dto.TransferAgentDAO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema (name = "TAGetResponse")
public class TAGetResponse {
    boolean success = false;
    int recordCount = 0;

    private List<TransferAgentDAO> transferAgentList;
    public TAGetResponse(boolean success) {
        this.success = success;
    }

    public TAGetResponse(boolean success, int count, List<TransferAgentDAO> lstTA) {

        this.success = success;
        recordCount = count;
        transferAgentList = lstTA;
    }
}

