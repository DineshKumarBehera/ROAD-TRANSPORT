package com.rbc.zfe0.road.services.transferitem.repository;

import com.rbc.zfe0.road.services.transferitem.entity.TransferAgentSecurityLink;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferAgentSecurityLinkRepository extends JpaRepository<TransferAgentSecurityLink, Integer> {
    TransferAgentSecurityLink findByAdpSecurityNumber(String adpSecurityNumber);
}
