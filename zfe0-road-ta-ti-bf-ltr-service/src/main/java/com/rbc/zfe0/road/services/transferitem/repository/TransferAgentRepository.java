package com.rbc.zfe0.road.services.transferitem.repository;

import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TransferAgentRepository extends JpaRepository<TransferAgent, Integer> {
    TransferAgent findByTransferAgentId(Integer agentId);

}
