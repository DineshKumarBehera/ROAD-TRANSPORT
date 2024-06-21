package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.TransferItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransferItemHistoryRepository extends JpaRepository<TransferItemHistory, Integer> {

    Optional<List<TransferItemHistory>> findByDispositionDtLessThan(Date dispositionDt);

    @Query(value = "select tih from TransferItemHistory tih where tih.transferItemHistoryId=?1")
    List<TransferItemHistory> deleteTransferItemHistory(Integer transferItemHistoryId);

    Optional<TransferItemHistory> findByTransferItemId(Integer transferItemId);
}
