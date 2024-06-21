package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.TransferItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferItemHistoryRepository extends JpaRepository<TransferItemHistory, Integer> {

}
