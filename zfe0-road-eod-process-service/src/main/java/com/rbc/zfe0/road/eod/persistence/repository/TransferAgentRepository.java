package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.TransferAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferAgentRepository extends JpaRepository<TransferAgent, Integer> {


    @Query(value = "Select ta from TransferAgent ta where ta.lastUseDt < ?1 and ta.transferAgentId not in (select ti.transferAgentId from TransferItem ti where ti.transferAgentId > 0)")
    List<TransferAgent> updateLastUsedTransferAgents(Date lastUsedDt);

    @Query(value = "Select ta from TransferAgent ta where ta.lastUpdateDt < ?1 and ta.transferAgentId not in (select ti.transferAgentId from TransferItem ti where ti.transferAgentId > 0)")
    List<TransferAgent> updateLastUpdateTransferAgents(Date lastUpdateDt);

    @Query(value="select ta from TransferAgent ta where ta.activeFlag=0 and (ta.lastUseDt < ?1)")
    List<TransferAgent> deleteTransferAgents(Date cleanupDate);

    TransferAgent findByTransferAgentId(Integer agentId);
}
