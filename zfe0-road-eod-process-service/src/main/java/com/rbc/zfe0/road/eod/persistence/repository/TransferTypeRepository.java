package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferTypeRepository extends JpaRepository<TransferType, String> {

    @Query(value="SELECT tt FROM TransferType tt where tt.tranCode=?1 order by tt.transferTypeName")
    List<TransferType> getTransferTypes(String tranCode);
}