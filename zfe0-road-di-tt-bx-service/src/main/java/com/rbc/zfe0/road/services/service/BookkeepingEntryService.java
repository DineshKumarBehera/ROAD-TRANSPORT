package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.transfertype.BookkeepingDTO;
import com.rbc.zfe0.road.services.dto.transfertype.TransferTypeDTO;
import com.rbc.zfe0.road.services.entity.BookkeepingEntry;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface BookkeepingEntryService {
    public List<BookkeepingEntry> addBookkeepingEntries(TransferTypeDTO form);
    public BookkeepingDTO getBookkeepingEntriesByCode(String ttCode);
    public List<BookkeepingEntry> updateBookkeepingEntries(TransferTypeDTO form);
}
