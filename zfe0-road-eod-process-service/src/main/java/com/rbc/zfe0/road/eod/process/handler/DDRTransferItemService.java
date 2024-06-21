package com.rbc.zfe0.road.eod.process.handler;

import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.process.handler.mapper.DDRTransferItem;

import java.util.List;

public interface DDRTransferItemService {
    public List<TransferItem> findByTransferTypeCode(String transferTypeCode);
}
