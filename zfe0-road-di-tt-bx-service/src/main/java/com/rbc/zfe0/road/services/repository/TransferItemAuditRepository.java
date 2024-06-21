package com.rbc.zfe0.road.services.repository;


import com.rbc.zfe0.road.services.entity.TransferItemAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferItemAuditRepository extends JpaRepository<TransferItemAudit, Integer> {

}