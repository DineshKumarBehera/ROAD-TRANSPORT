package com.rbc.zfe0.road.services.transferitem.repository;


import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.entity.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferTypeRepository extends JpaRepository<TransferType, String> {
    TransferType findByTransferTypeCode(String codeType);

    @Query("SELECT tt FROM TransferType tt WHERE tt.uiActiveFlag > :uiActiveFlag ORDER BY tt.transferTypeName")
    List<TransferType> findByActiveTransferTypeByUiActiveFlag(@Param("uiActiveFlag") Integer activeFlag);

    @Query("SELECT tt FROM TransferType tt WHERE tt.tranCode = :tranCode ORDER BY tt.transferTypeName")
    List<TransferType> getTransferTypesByTranCode(@Param("tranCode") String tranCode);

    List<TransferType> findTransferTypeNameByTranCode(String transferType);


}
