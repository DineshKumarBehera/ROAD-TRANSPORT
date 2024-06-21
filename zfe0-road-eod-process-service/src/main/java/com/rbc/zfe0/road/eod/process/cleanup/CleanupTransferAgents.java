package com.rbc.zfe0.road.eod.process.cleanup;

import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.persistence.entity.TransferAgent;
import com.rbc.zfe0.road.eod.persistence.entity.TransferAgentSecurityLink;
import com.rbc.zfe0.road.eod.persistence.repository.TransferAgentRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferAgentSecurityLinkRepository;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class CleanupTransferAgents {

    @Value("${rbc.road.cleanup.ta-delete-cleanup-months}")
    private String taDeleteCleanupPeriod;

    @Value("${rbc.road.cleanup.ta-delete-last-used-months}")
    private String taDeleteLastUsedPeriod;

    @Value("${rbc.road.cleanup.ta-delete-last-updated-months}")
    private String taDeleteLastUpdatedPeriod;

    @Autowired
    TransferAgentRepository transferAgentRepository;

    @Autowired
    TransferAgentSecurityLinkRepository transferAgentSecurityLinkRepository;

    /*
     * Delete transferagents from the system.
     * All the parameters for deleting Transferagents period are set
     * in the properties file.
     */
    public void deleteTransferAgents(List errorList) {
        try {
            Date cleanupDate = Utility.addMontsToCurrentDate(-
                    Utility.getInteger(taDeleteCleanupPeriod));
            Date lastUsedDt = Utility.addMontsToCurrentDate(-
                    Utility.getInteger(taDeleteLastUsedPeriod));
            Date lastUpdatedDt = Utility.addMontsToCurrentDate(-
                    Utility.getInteger(taDeleteLastUpdatedPeriod));
            log.info("Transfer Agents cleanupDate: {}", cleanupDate);
            log.info("Transfer Agents lastUsedDt: {}", lastUsedDt);
            log.info("Transfer Agents lastUpdatedDt: {}", lastUpdatedDt);
            deleteTransferAgents(lastUsedDt, lastUpdatedDt, cleanupDate);
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: process failed deleting transfer agents "
                    + t.getMessage(), t);
            errorList.add(re);
            log.error("EOD process failed while deleting transfer agents", t);
        }
    }

    private void deleteTransferAgents(Date lastUsedDt, Date lastUpdatedDt, Date cleanupDate) {
        deleteLastUsedTransferAgents(lastUsedDt);
        deleteLastUpdateTransferAgent(lastUpdatedDt);
        deleteLastUpdateSecurityLink(lastUpdatedDt);
        deleteLastUsedSecurityLink(lastUsedDt);
        deleteTransferAgents(cleanupDate);
    }

    private void deleteTransferAgents(Date cleanupDate) {
        List<TransferAgent> taList = transferAgentRepository.deleteTransferAgents(cleanupDate);
        for (TransferAgent ta : taList) {
            log.info("TransferAgent Id: {}", ta.getTransferAgentId());
        }
    }

    private void deleteLastUsedTransferAgents(Date lastUsedDt) {
        List<TransferAgent> taList = transferAgentRepository.updateLastUsedTransferAgents(lastUsedDt);
        for (TransferAgent ta : taList) {
            log.info("Last Used TransferAgent Id: {}", ta.getTransferAgentId());
        }
    }

    private void deleteLastUpdateTransferAgent(Date lastUpdatedDt) {
        List<TransferAgent> taList = transferAgentRepository.updateLastUpdateTransferAgents(lastUpdatedDt);
        for (TransferAgent ta : taList) {
            log.info("Last Update TransferAgent Id: {}", ta.getTransferAgentId());
        }
    }

    private void deleteLastUpdateSecurityLink(Date lastUpdatedDt) {
        List<TransferAgentSecurityLink> slList = transferAgentSecurityLinkRepository.deleteLastUpdateSecurityLink(lastUpdatedDt);
        for (TransferAgentSecurityLink sl : slList) {
            log.info("Last Update TransferAgentSecurityLink Id: {}", sl.getTransferAgentSecurityLinkId());
        }
    }

    private void deleteLastUsedSecurityLink(Date lastUsedDt) {
        List<TransferAgentSecurityLink> slList = transferAgentSecurityLinkRepository.deleteLastUsedSecurityLink(lastUsedDt);
        for (TransferAgentSecurityLink sl : slList) {
            log.info("Last Used TransferAgentSecurityLink Id: {}", sl.getTransferAgentSecurityLinkId());
        }
    }

}
