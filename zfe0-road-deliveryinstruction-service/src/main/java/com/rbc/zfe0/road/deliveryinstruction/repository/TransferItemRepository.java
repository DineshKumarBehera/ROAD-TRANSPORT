package com.rbc.zfe0.road.deliveryinstruction.repository;

import com.rbc.zfe0.road.deliveryinstruction.model.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TransferItemRepository provides the mechanism for storage, retrieval, search, update and delete operation on objects.
 */
public interface TransferItemRepository extends JpaRepository<TransferItem, Integer> {
    long countByDeliveryInstructionId(Integer deliveryInstructionId);
}
