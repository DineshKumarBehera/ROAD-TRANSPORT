package com.rbc.zfe0.road.services.transferitem.repository;

import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EnableJpaRepositories
public interface TransferItemHistoryRepository extends JpaRepository<TransferItemHistory, Integer>, JpaSpecificationExecutor<TransferItemHistory> {
    List<TransferItemHistory> findByStatusCode(String statusCode);

    TransferItemHistory findById(int id);

    TransferItemHistory findByTransferItemId(int itemId);
    TransferItemHistory findTransferItemIdByTransferItemHistoryId(int historyId);
}
