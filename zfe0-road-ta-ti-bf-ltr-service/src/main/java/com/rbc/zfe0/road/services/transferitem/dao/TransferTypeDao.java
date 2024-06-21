package com.rbc.zfe0.road.services.transferitem.dao;

import com.rbc.zfe0.road.services.transferitem.entity.TransferType;
import com.rbc.zfe0.road.services.transferitem.exception.ResourceNotFoundException;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.repository.TransferTypeRepository;
import com.rbc.zfe0.road.services.transferitem.service.TransferTypeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TransferTypeDao {

    @Autowired
    private TransferTypeRepository transferTypeRepository;

    @Autowired
    TransferTypeService transferTypeService;


    public TransferType getTransferTypeByCode(String transferTypeCode) {
        try {
            return transferTypeRepository.findByTransferTypeCode(transferTypeCode);
        } catch (Exception e) {
            throw new ResourceNotFoundException("TransferType", "code", transferTypeCode);
        }

    }

    public TransferType saveTransferType(TransferType transferType) {
        return transferTypeRepository.save(transferType);
    }

    public TransferType updateTransferTypeProperties(TransferType transferType, EditTransferItem editTransferItem) {
        transferType.setLastUpdateDt(new Date());
        if (transferType != null) {
            transferType.setLastUpdateDt(new Date());
            transferType.setLastUpdateName(editTransferItem.getTransferTypeResponse().getLastUpdateName());
            transferType = transferTypeService.saveTransferType(transferType, editTransferItem.getTransferTypeResponse().getLastUpdateName());
        }
        return transferType;
    }

}

