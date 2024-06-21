package com.rbc.zfe0.road.services.transferitem.service;

import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface TransferAgentService {
    TransferAgent saveTransferAgent(TransferAgent transferAgent, String user);

    void associateTransferAgent(TransferAgent agent, EditTransferItem editTransferItem, String user);

    TransferAgent getTransferAgent(String originalAdpSecNo);
    Optional<TransferAgent> getTransferAgentDetails(int agentId);
}
