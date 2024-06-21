package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.dto.entry.EntryCodeRequest;
import com.rbc.zfe0.road.services.dto.entry.EntryCodeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface EntryCodeService {
    public void updateEntryCode(Integer entryCodeId, EntryCodeRequest entryCodeReq);
    public void createEntryCode(EntryCodeRequest entryCodeReq);
    public Map<String, String> deleteByEntryCodeId(Integer entryCodeId);
    public List<EntryCodeResponse> getAllEntryCodes();
}
