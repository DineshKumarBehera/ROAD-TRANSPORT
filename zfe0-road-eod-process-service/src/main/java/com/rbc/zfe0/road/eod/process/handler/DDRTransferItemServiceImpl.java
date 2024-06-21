package com.rbc.zfe0.road.eod.process.handler;

import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DDRTransferItemServiceImpl implements DDRTransferItemService {

    @Autowired
    private TransferItemRepository ddrSchedulerJobRepository;

    @Override
    public List<TransferItem> findByTransferTypeCode(String transferTypeCode) {
        return ddrSchedulerJobRepository.findByTransferTypeCode(transferTypeCode);
    }
}
