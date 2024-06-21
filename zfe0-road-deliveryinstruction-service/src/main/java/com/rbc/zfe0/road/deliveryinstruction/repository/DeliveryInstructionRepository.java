package com.rbc.zfe0.road.deliveryinstruction.repository;

import com.rbc.zfe0.road.deliveryinstruction.model.DeliveryInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DeliveryInstructionRepository provides the mechanism for storage, retrieval, search, update and delete operation on objects.
 */
@Repository
public interface DeliveryInstructionRepository extends JpaRepository<DeliveryInstruction, Integer> {
    List<DeliveryInstruction> findAll();
}
