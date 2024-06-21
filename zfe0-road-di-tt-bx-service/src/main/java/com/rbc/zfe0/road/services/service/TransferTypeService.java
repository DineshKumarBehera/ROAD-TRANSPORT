package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.transfertype.TransferTypeDTO;
import com.rbc.zfe0.road.services.entity.TransferType;
import com.rbc.zfe0.road.services.exception.TransferTypeException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransferTypeService {
    public void createTransferType(TransferTypeDTO form) throws TransferTypeException;
    public List<TransferType> getAllTransferTypes();
    public List<TransferType> getActiveTransferTypes();
    public TransferTypeDTO getByTransferTypeCode(String transferTypeCode);
    public void updateTransferType(String transferTypeCode, TransferTypeDTO form);
    public void deleteByTransferTypeCode(String transferTypeCode, TransferTypeDTO form);
}
