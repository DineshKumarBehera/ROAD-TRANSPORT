package com.rbc.zfe0.road.services.transferitem.service;

import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.repository.TransferAgentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class TransferAgentServiceImpl implements TransferAgentService {
    @Autowired
    TransferAgentRepository transferAgentRepository;

    @Override
    public TransferAgent saveTransferAgent(TransferAgent transferAgent, String user) {
        transferAgent.setLastUpdateName(user);
        return transferAgentRepository.save(transferAgent);
    }

    @Override
    public void associateTransferAgent(TransferAgent agent, EditTransferItem editTransferItem, String user) {

    }

    @Override
    public TransferAgent getTransferAgent(String originalAdpSecNo) {
        return null;
    }

    @Override
    public Optional<TransferAgent> getTransferAgentDetails(int agentId) {
        return transferAgentRepository.findById(agentId);
    }

    public TransferAgent getTransAgentId(TransferAgent transAgentId) {
        return transAgentId;
    }
}
