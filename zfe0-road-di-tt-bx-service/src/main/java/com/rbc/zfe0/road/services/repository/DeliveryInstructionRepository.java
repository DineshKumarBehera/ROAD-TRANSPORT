package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.DeliveryInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DeliveryInstructionRepository provides the mechanism for storage, retrieval, search, updateEntryCode and delete operation on objects.
 */
@Repository
public interface DeliveryInstructionRepository extends JpaRepository<DeliveryInstruction, Integer> {
    List<DeliveryInstruction> findAll();
}
