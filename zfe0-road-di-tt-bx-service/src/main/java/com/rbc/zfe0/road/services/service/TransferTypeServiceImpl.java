package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.transfertype.*;
import com.rbc.zfe0.road.services.entity.BatchEntry;
import com.rbc.zfe0.road.services.entity.BookkeepingEntry;
import com.rbc.zfe0.road.services.entity.TransferType;
import com.rbc.zfe0.road.services.exception.TransferTypeException;
import com.rbc.zfe0.road.services.repository.BatchEntryRepository;
import com.rbc.zfe0.road.services.repository.BookkeepingEntryRepository;
import com.rbc.zfe0.road.services.repository.TransferTypeRepository;
import com.rbc.zfe0.road.services.utils.TransferTypeUtil;
import com.rbc.zfe0.road.services.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class TransferTypeServiceImpl implements TransferTypeService {
    @Autowired
    private TransferTypeRepository transferTypeRepository;
    @Autowired
    private BookkeepingEntryService bookkeepingEntryService;
    @Autowired
    private BatchEntryService batchEntryService;
    @Autowired
    private BookkeepingEntryRepository bookkeepingEntryRepository;
    @Autowired
    private BatchEntryRepository batchEntryRepository;

    @Override
    public List<TransferType> getAllTransferTypes() {
        return transferTypeRepository.findByTransferTypeCodeNotIn(Arrays.asList(Constants.TRANSFER_TYPE_BOX_LOCATION, Constants.TRANSFER_TYPE_ENTRY_CODE));
    }

    @Override
    public List<TransferType> getActiveTransferTypes() {
        return transferTypeRepository.findByUiActiveFlagAndTransferTypeCodeNotIn(1, Arrays.asList(Constants.TRANSFER_TYPE_BOX_LOCATION, Constants.TRANSFER_TYPE_ENTRY_CODE));
    }

    @Override
    public void createTransferType(TransferTypeDTO form) throws TransferTypeException {
        if (StringUtils.isBlank(form.getTwoCharCode())) {
            throw new TransferTypeException("Two char code is empty");
        }
        if (StringUtils.isBlank(form.getDescription())) {
            throw new TransferTypeException("Description is empty.");
        }
        if (!form.getIsCashIssues() && !form.getIsSecurityIssues()) {
            throw new TransferTypeException("Transfer type should be cash issues, security issues, or both.");
        }
        if (TransferTypeUtil.getTransferTypeCodeFromFormData(form) != null) {
            if (TransferTypeUtil.getTransferTypeCodeFromFormData(form).equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION)
                    || TransferTypeUtil.getTransferTypeCodeFromFormData(form).equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE)) {
                throw new TransferTypeException("Transfer type code " + TransferTypeUtil.getTransferTypeCodeFromFormData(form) + " cannot be created.");
            }
            Optional<TransferType> transferType = transferTypeRepository.findById(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
            if (transferType.isEmpty()) {
                TransferType tt = new TransferType();
                tt.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                tt.setTransferTypeName(TransferTypeUtil.getTransferTypeNameFromFormData(form));
                tt.setTranCode(TransferTypeUtil.getTransferTypeTranCodeFromFormData(form));
                tt.setUiActiveFlag(Utility.convertBooleanToInteger(form.getUiActive()));
                tt.setAutoRoadBlockFlag(Utility.convertBooleanToInteger(form.getAutoRoadBlock()));
                tt.setLastUpdateName(form.getLastUpdateName());
                tt.setLastUpdateDt(new Date());
                List<BookkeepingEntry> bookkeepingEntries = bookkeepingEntryService.addBookkeepingEntries(form);
                List<BatchEntry> batchEntries = batchEntryService.addBatchEntries(form);
                saveTransferTypeDetails(tt, bookkeepingEntries, batchEntries);
            } else {
                throw new TransferTypeException("Transfer Type code is already existing.");
            }
        } else {
            throw new TransferTypeException("Transfer Type code is null");
        }
    }

    @Override
    public TransferTypeDTO getByTransferTypeCode(String transferTypeCode) {
        TransferTypeDTO ttDTO = new TransferTypeDTO();
        if (StringUtils.isBlank(transferTypeCode) || transferTypeCode.equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION) || transferTypeCode.equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE)) {
            throw new TransferTypeException("Invalid Transfer Type Code.");
        } else {
            Optional<TransferType> transferType = transferTypeRepository.findById(transferTypeCode);
            if (transferType.isPresent()) {
                ttDTO.setCode(transferTypeCode);
                ttDTO.setTransferTypeCode(transferType.get().getTransferTypeCode());
                ttDTO.setTwoCharCode(!StringUtils.isBlank(transferType.get().getTransferTypeCode()) ? transferType.get().getTransferTypeCode().substring(0, 2) : null);
                ttDTO.setDescription(transferType.get().getTransferTypeName());
                Map<String, Boolean> cashSecType = TransferTypeUtil.getCashSecurityDataFromTranCode(transferType.get());
                ttDTO.setIsCashIssues(cashSecType != null ? cashSecType.get(Constants.TRANSFER_TYPE_CASH) : false);
                ttDTO.setIsSecurityIssues(cashSecType != null ? cashSecType.get(Constants.TRANSFER_TYPE_SECURITY) : false);
                ttDTO.setUiActive(transferType.get().getUiActiveFlag() == 1);
                ttDTO.setAutoRoadBlock(transferType.get().getAutoRoadBlockFlag() == 1);
                ttDTO.setLastUpdateName(transferType.get().getLastUpdateName());
                ttDTO.setLastUpdateDt(new Date());
                BookkeepingDTO bkDTO = bookkeepingEntryService.getBookkeepingEntriesByCode(transferType.get().getTransferTypeCode());
                ttDTO.setBookkeeping(bkDTO);
                BatchEntryDTO beDTO = batchEntryService.getBatchEntriesByCode(transferType.get().getTransferTypeCode());
                ttDTO.setBatchEntry(beDTO);
            }
        }
        return ttDTO;
    }

    @Override
    public void updateTransferType(String transferTypeCode, TransferTypeDTO form) {
        if (StringUtils.isBlank(transferTypeCode) || transferTypeCode.equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION) || transferTypeCode.equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE)) {
            throw new TransferTypeException("Invalid Transfer Type Code.");
        } else {
            Optional<TransferType> transferType = transferTypeRepository.findById(transferTypeCode);
            if (transferType.isPresent()) {
                transferType.get().setUiActiveFlag(Utility.convertBooleanToInteger(form.getUiActive()));
                transferType.get().setAutoRoadBlockFlag(Utility.convertBooleanToInteger(form.getAutoRoadBlock()));
                transferType.get().setLastUpdateName(form.getLastUpdateName());
                transferType.get().setLastUpdateDt(new Date());
                List<BookkeepingEntry> bookkeepingEntries = bookkeepingEntryService.updateBookkeepingEntries(form);
                List<BatchEntry> batchEntries = batchEntryService.updateBatchEntries(form);
                saveTransferTypeDetails(transferType.get(), bookkeepingEntries, batchEntries);
            }
        }
    }

    @Override
    public void deleteByTransferTypeCode(String transferTypeCode, TransferTypeDTO form) {
        if (StringUtils.isBlank(transferTypeCode) || transferTypeCode.equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION) || transferTypeCode.equalsIgnoreCase(Constants.TRANSFER_TYPE_ENTRY_CODE)) {
            throw new TransferTypeException("Invalid Transfer Type Code.");
        } else {
            Optional<TransferType> transferType = transferTypeRepository.findById(transferTypeCode);
            if (transferType.isPresent()) {
                transferType.get().setUiActiveFlag(0);
                transferType.get().setAutoRoadBlockFlag(0);
                transferType.get().setLastUpdateName(form.getLastUpdateName());
                transferType.get().setLastUpdateDt(new Date());
                transferTypeRepository.save(transferType.get());
            }
        }
    }

    public void saveTransferTypeDetails(TransferType tt, List<BookkeepingEntry> bookkeepingEntries, List<BatchEntry> batchEntries) {
        transferTypeRepository.save(tt);
        bookkeepingEntryRepository.saveAll(bookkeepingEntries);
        batchEntryRepository.saveAll(batchEntries);
    }
}
