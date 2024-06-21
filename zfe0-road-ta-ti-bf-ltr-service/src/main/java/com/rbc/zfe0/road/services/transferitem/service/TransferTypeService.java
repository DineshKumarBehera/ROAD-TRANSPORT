package com.rbc.zfe0.road.services.transferitem.service;


import com.rbc.zfe0.road.services.transferitem.entity.TransferAgent;
import com.rbc.zfe0.road.services.transferitem.entity.TransferType;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.model.TransferTypeDetails;
import com.rbc.zfe0.road.services.transferitem.repository.TransferTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface TransferTypeService {
    public List<TransferType> getAllTransferTypes();
    void insert(TransferTypeDetails transferTypedetails);
    TransferType saveTransferType(TransferType transferType, String user);


}
