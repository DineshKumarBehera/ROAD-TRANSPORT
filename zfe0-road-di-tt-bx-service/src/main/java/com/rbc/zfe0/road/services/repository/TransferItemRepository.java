package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferItemRepository extends JpaRepository<TransferItem, Integer> {

    List<TransferItem> findByStatusCode(String statusCode);

    Long countByDeliveryInstructionId(Integer deliveryInstructionId);

}