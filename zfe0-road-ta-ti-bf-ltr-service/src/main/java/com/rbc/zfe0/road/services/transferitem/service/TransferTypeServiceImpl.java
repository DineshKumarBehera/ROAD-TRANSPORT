package com.rbc.zfe0.road.services.transferitem.service;

import com.rbc.zfe0.road.services.transferitem.entity.TransferType;
import com.rbc.zfe0.road.services.transferitem.model.TransferTypeDetails;
import com.rbc.zfe0.road.services.transferitem.repository.TransferTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransferTypeServiceImpl implements TransferTypeService {
    @Autowired
    TransferTypeRepository transferTypeRepository;

    @Override
    public List<TransferType> getAllTransferTypes() {
        return transferTypeRepository.findAll();
    }

    @Override
    public void insert(TransferTypeDetails transferTypedetails) {

    }

    @Override
    public TransferType saveTransferType(TransferType transferType, String user) {
        return transferTypeRepository.save(transferType);
    }

}

