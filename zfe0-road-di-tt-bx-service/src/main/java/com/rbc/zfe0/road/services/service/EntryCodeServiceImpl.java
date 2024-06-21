package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.entry.EntryCodeRequest;
import com.rbc.zfe0.road.services.dto.entry.EntryCodeResponse;
import com.rbc.zfe0.road.services.entity.BatchEntry;
import com.rbc.zfe0.road.services.exception.EntryCodeException;
import com.rbc.zfe0.road.services.repository.BatchEntryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
@NoArgsConstructor
public class EntryCodeServiceImpl implements EntryCodeService {
    @Autowired
    private BatchEntryRepository batchEntryRepository;

    @Override
    public List<EntryCodeResponse> getAllEntryCodes() {
        Optional<List<BatchEntry>> batchEntries = batchEntryRepository.findByTransferTypeCode(Constants.TRANSFER_TYPE_ENTRY_CODE);
        List<EntryCodeResponse> responses = new ArrayList<>();
        batchEntries.ifPresent(bkEntries -> bkEntries.forEach(bk -> {
            EntryCodeResponse response = new EntryCodeResponse();
            response.setEntryCodeId(bk.getBatchEntryId());
            response.setEntryCodeName(bk.getEntryCode() != null ? bk.getEntryCode().trim() : null);
            response.setUserChoice(bk.getStatusCode() != null && bk.getStatusCode().equalsIgnoreCase(Constants.BATCH_ENTRY_USER_CHOICE_VALUE));
            responses.add(response);
        }));
        return responses;
    }

    @Override
    public void updateEntryCode(Integer entryCodeId, EntryCodeRequest entryCodeReq) {
        if (entryCodeId != null && !StringUtils.isBlank(entryCodeReq.getEntryCodeName())) {
            Optional<BatchEntry> batchEntry = batchEntryRepository.findById(entryCodeId);
            if (batchEntry.isPresent()) {
                if (batchEntry.get().getTransferTypeCode().equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE)) {
                    batchEntry.get().setEntryCode(entryCodeReq.getEntryCodeName());
                    if (entryCodeReq.getUserChoice()) {
                        batchEntry.get().setStatusCode(Constants.BATCH_ENTRY_USER_CHOICE_VALUE);
                    } else {
                        batchEntry.get().setStatusCode(null);
                    }
                    batchEntry.get().setLastUpdateName(entryCodeReq.getUsername());
                    batchEntryRepository.save(batchEntry.get());
                } else {
                    throw new EntryCodeException("Entry code does not exist.");
                }
            } else {
                throw new EntryCodeException("Entry code id does not exist.");
            }
        } else {
            throw new EntryCodeException("Entry Code is missing in the request.");
        }
    }

    @Override
    public void createEntryCode(EntryCodeRequest entryCodeReq) {
        if (!StringUtils.isBlank(entryCodeReq.getEntryCodeName())
                && !StringUtils.isBlank(entryCodeReq.getUsername())) {
            BatchEntry batchEntry = new BatchEntry();
            batchEntry.setTransferTypeCode(Constants.TRANSFER_TYPE_ENTRY_CODE);
            batchEntry.setEntryCode(entryCodeReq.getEntryCodeName());
            if (entryCodeReq.getUserChoice()) {
                batchEntry.setStatusCode(Constants.BATCH_ENTRY_USER_CHOICE_VALUE);
            }
            batchEntry.setDummyAccountFlag(0);
            batchEntry.setOriginalIssueFlag(0);
            batchEntry.setBox97EntryFlag(0);
            batchEntry.setDbUpdateFlag(0);
            batchEntry.setDeleteOneCsEntryFlag(0);
            batchEntry.setLastUpdateName(entryCodeReq.getUsername());
            batchEntryRepository.save(batchEntry);
        } else {
            throw new EntryCodeException("Entry code name or username is missing in the request.");
        }
    }

    @Override
    public Map<String, String> deleteByEntryCodeId(Integer entryCodeId) {
        Map<String, String> message = new HashMap<>();
        Optional<BatchEntry> entryCode = batchEntryRepository.findById(entryCodeId);
        if (entryCode.isPresent() && entryCode.get().getTransferTypeCode().equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE)) {
            Optional<List<BatchEntry>> batchEntries = batchEntryRepository.findByEntryCodeAndTransferTypeCodeNotIn(entryCode.get().getEntryCode(), Arrays.asList(Constants.TRANSFER_TYPE_ENTRY_CODE, Constants.TRANSFER_TYPE_BOX_LOCATION));
            if (batchEntries.get().size() > 0) {
                Map<String, Long> countByTTCode = batchEntries.get()
                        .stream()
                        .filter(t -> !t.getTransferTypeCode().equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE) && !t.getTransferTypeCode().equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION))
                        .collect(Collectors.groupingBy(BatchEntry::getTransferTypeCode, Collectors.counting()));
                log.info("Count of entry code assigned to transfer types(s) {}", countByTTCode.keySet());
                message.put(Constants.Exist, "Entry code: " + entryCode.get().getEntryCode() + " - cannot be deleted. It is in use by transfer type(s): " + countByTTCode.keySet());
            } else {
                batchEntryRepository.deleteById(entryCode.get().getBatchEntryId());
                message.put(Constants.Exist, "The selected entry code is deleted successfully.");
            }
        } else {
            message.put(Constants.NotExist, "The selected entry code does not exist.");
        }
        return message;
    }
}

