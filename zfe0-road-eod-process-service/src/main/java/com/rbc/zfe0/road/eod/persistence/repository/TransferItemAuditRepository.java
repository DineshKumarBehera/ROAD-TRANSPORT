package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.TransferItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferItemAuditRepository extends JpaRepository<TransferItemAudit, Integer> {

    @Query("SELECT tia FROM com.rbc.zfe0.road.eod.persistence.entity.TransferItemAudit tia WHERE tia.transferItemId IN (:transferItemIds)")
    List<TransferItemAudit> findByTransferItems(@Param("transferItemIds") List<Integer> transferItemIds);

    List<TransferItemAudit> findByTransferItemId(Integer transferItemId);
}
