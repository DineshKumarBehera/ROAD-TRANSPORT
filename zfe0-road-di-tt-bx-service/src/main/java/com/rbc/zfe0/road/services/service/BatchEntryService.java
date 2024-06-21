package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.transfertype.BatchEntryDTO;
import com.rbc.zfe0.road.services.dto.transfertype.TransferTypeDTO;
import com.rbc.zfe0.road.services.entity.BatchEntry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BatchEntryService {
    public BatchEntryDTO getBatchEntriesByCode(String transferTypeCode);
    public List<BatchEntry> addBatchEntries(TransferTypeDTO form);
    public List<BatchEntry> updateBatchEntries(TransferTypeDTO form);
}
