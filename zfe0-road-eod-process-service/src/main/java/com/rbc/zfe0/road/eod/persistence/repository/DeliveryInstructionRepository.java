package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.DeliveryInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DeliveryInstructionRepository provides the mechanism for storage, retrieval, search, update and delete operation on objects.
 */
@Repository
public interface DeliveryInstructionRepository extends JpaRepository<DeliveryInstruction, Integer> {
    DeliveryInstruction findByDeliveryInstructionId(Integer delivaryInstructionId);
}
