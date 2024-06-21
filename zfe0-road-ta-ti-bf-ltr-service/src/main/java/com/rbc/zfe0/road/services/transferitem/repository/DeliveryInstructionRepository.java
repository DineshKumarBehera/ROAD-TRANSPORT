package com.rbc.zfe0.road.services.transferitem.repository;


import com.rbc.zfe0.road.services.transferitem.entity.DeliveryInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DeliveryInstructionRepository provides the mechanism for storage, retrieval, search, update and delete operation on objects.
 */
@Repository
public interface DeliveryInstructionRepository extends JpaRepository<DeliveryInstruction, Integer> {
    DeliveryInstruction findByDeliveryInstructionId(Integer delivaryInstructionId);
}
