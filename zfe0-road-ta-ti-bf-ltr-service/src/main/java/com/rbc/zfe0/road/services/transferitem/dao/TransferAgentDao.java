package com.rbc.zfe0.road.services.transferitem.dao;

import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.entity.TransferAgentSecurityLink;
import com.rbc.zfe0.road.services.transferitem.exception.ResourceNotFoundException;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.repository.TransferAgentRepository;
import com.rbc.zfe0.road.services.transferitem.repository.TransferAgentSecurityLinkRepository;
import com.rbc.zfe0.road.services.transferitem.service.TransferAgentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TransferAgentDao {

    @Autowired
    private TransferAgentRepository transferAgentRepository;
    @Autowired
    TransferAgentService transferAgentService;

    @Autowired
    private TransferAgentSecurityLinkRepository transferAgentSecurityLinkRepository;


    public TransferAgent getTransferAgentById(Integer transferAgentId) {
        return transferAgentRepository.findById(transferAgentId)
                .orElseThrow(() -> new ResourceNotFoundException("TransferAgent", "id", transferAgentId));
    }

    public TransferAgent saveTransferAgent(TransferAgent transferAgent) {
        return transferAgentRepository.save(transferAgent);
    }

    public TransferAgent updateTransferAgentProperties(TransferAgent transferAgent, EditTransferItem editTransferItem) {
        if(transferAgent != null) {
            transferAgent.setLastUseDt(new Date());
            transferAgent.setTransferAgentName(editTransferItem.getTransferAgentDetails().getTransferAgentName());
            transferAgent.setLastUpdateName(editTransferItem.getTransferAgentDetails().getLastUpdateName());
            transferAgent.setLastUpdateDt(new Date());
            transferAgentService.saveTransferAgent(transferAgent, editTransferItem.getTransferAgentDetails().getLastUpdateName());
        }
        return saveTransferAgent(transferAgent);
    }

    public void saveAndAssociateTransferAgent(TransferAgent agentIn, String adpSecurityNumber, String user) {

        TransferAgent currentAgent = getAssociatedTransferAgent(adpSecurityNumber);

        if (currentAgent != null && !currentAgent.equals(agentIn)) {
            deleteAssociation(adpSecurityNumber);
        } else if (currentAgent != null && currentAgent.equals(agentIn)) {
            return;
        }

        createAssociation(agentIn, adpSecurityNumber, user);
    }


    public TransferAgent getAssociatedTransferAgent(String adpSecurityNumber) {
        TransferAgentSecurityLink link = transferAgentSecurityLinkRepository.findByAdpSecurityNumber(adpSecurityNumber);
        log.info("link   " + link);
        return link != null ? link.getTransferAgent() : null;
    }


    private void deleteAssociation(String adpSecurityNumber) {
        TransferAgentSecurityLink link = transferAgentSecurityLinkRepository.findByAdpSecurityNumber(adpSecurityNumber);
        if (link != null) {
            transferAgentSecurityLinkRepository.delete(link);
        }
    }


    public void createAssociation(TransferAgent agentIn, String adpSecurityNumber, String user) {
        TransferAgentSecurityLink link = new TransferAgentSecurityLink();
        link.setTransferAgent(agentIn);
        link.setAdpSecurityNumber(adpSecurityNumber);
        link.setLastUpdateName(user);
        link.setLastUpdateDt(new Date());
        transferAgentSecurityLinkRepository.save(link);
    }
}
