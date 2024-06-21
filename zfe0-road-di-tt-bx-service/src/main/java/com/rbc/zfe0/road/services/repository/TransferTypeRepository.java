package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferTypeRepository extends JpaRepository<TransferType, String> {

    List<TransferType> findByTransferTypeCodeNotIn(List<String> excludeTypeCodes);

    List<TransferType> findByUiActiveFlagAndTransferTypeCodeNotIn(Integer uiActiveFlag, List<String> excludeTypeCodes);
}
