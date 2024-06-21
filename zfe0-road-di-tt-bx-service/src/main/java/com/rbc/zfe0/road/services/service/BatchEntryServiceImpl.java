package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.entry.EntryAcctNbr;
import com.rbc.zfe0.road.services.dto.enumtype.CodeType;
import com.rbc.zfe0.road.services.dto.enumtype.DebitCreditType;
import com.rbc.zfe0.road.services.dto.enumtype.IssueTypeCode;
import com.rbc.zfe0.road.services.dto.enumtype.RecordType;
import com.rbc.zfe0.road.services.dto.transfertype.*;
import com.rbc.zfe0.road.services.entity.BatchEntry;
import com.rbc.zfe0.road.services.repository.BatchEntryRepository;
import com.rbc.zfe0.road.services.utils.TransferTypeUtil;
import com.rbc.zfe0.road.services.utils.Utility;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class BatchEntryServiceImpl implements BatchEntryService{
    @Autowired
    private  BatchEntryRepository batchEntryRepository;
    @Override
    public BatchEntryDTO getBatchEntriesByCode(String transferTypeCode) {
        BatchEntryDTO beDTO = new BatchEntryDTO();
        beDTO.setBeottc(getBatchEntriesForOTTClient(transferTypeCode));
        beDTO.setBeottd(getBatchEntriesForOTTDummy(transferTypeCode));
        beDTO.setBeclosc(getBatchEntriesForCloseClient(transferTypeCode));
        beDTO.setBeclosd(getBatchEntriesForCloseDummy(transferTypeCode));
        beDTO.setBeclosc97(getBatchEntriesForCloseClient97(transferTypeCode));
        beDTO.setBeclosd97(getBatchEntriesForCloseDummy97(transferTypeCode));
        return beDTO;
    }

    public List<BatchEntry> addBatchEntries(TransferTypeDTO form) {
        List<BatchEntry> batchEntries = new ArrayList<>();
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeottc().getOttEntry(), Constants.STATUS_OUT_TO_TRANSFER, 0, null));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeottc().getInto97Entry(), Constants.STATUS_OUT_TO_TRANSFER, 0, null));

        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeottd().getOttEntry(), Constants.STATUS_OUT_TO_TRANSFER, 1, null));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeottd().getInto97Entry(), Constants.STATUS_OUT_TO_TRANSFER, 1, null));

        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc().getNormalClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc().getEscheatedClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc().getWorthlessClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc().getRejectedClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc().getConfiscatedClose(), Constants.STATUS_CLOSED, 0, 0));

        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd().getNormalClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd().getEscheatedClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd().getWorthlessClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd().getRejectedClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd().getConfiscatedClose(), Constants.STATUS_CLOSED, 1, 0));

        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc97().getNormalClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc97().getEscheatedClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc97().getWorthlessClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc97().getRejectedClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosc97().getConfiscatedClose(), Constants.STATUS_CLOSED, 0, 1));

        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd97().getNormalClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd97().getEscheatedClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd97().getWorthlessClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd97().getRejectedClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(addBatchEntry(form, form.getBatchEntry().getBeclosd97().getConfiscatedClose(), Constants.STATUS_CLOSED, 1, 1));

        return batchEntries;
    }

    private List<BatchEntry> addBatchEntry(TransferTypeDTO form,
                                           List<Entry> entry, String statusCode,
                                           Integer dummyAccountFlag,
                                           Integer box97EntryFlag) {
        List<BatchEntry> batchEntries = new ArrayList<>();
        if (entry != null && !entry.isEmpty()) {
            for (Entry e : entry) {
                BatchEntry batchEntry = new BatchEntry();
                EntryAcctNbr entryAcctNbr = new EntryAcctNbr(e.getAcctBoxLoc());
                batchEntry.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                batchEntry.setLastUpdateName(form.getLastUpdateName());
                batchEntry.setLastUpdateDt(new Date());
                batchEntry.setEntryCode(e.getEntryCode());
                batchEntry.setBatchCode(e.getBatchCode());
                batchEntry.setStatusCode(statusCode);
                batchEntry.setMemoAccountNumber(entryAcctNbr.getEntryAcctNbr());
                batchEntry.setMemoAccountType(entryAcctNbr.getEntryAcctType());
                batchEntry.setMemoAccountCheckDigit(entryAcctNbr.getEntryAcctCheckDigit());
                batchEntry.setMemoType(TransferTypeUtil.generateMemoType(e.getDebitOrCredit(), e.getRecordType()));
                batchEntry.setSharesFromCode(TransferTypeUtil.generateSharesFromCode(e.getQty(), e.getDollars(), e.getSecurity()));
                batchEntry.setDeleteOneCsEntryFlag(Utility.convertBooleanToInteger(e.getCse()));
                batchEntry.setDbUpdateFlag(Utility.convertBooleanToInteger(e.getUpdateDb()));
                batchEntry.setIssueTypeCode(e.getEntryTypeIssueCode().toString());
                batchEntry.setDummyAccountFlag(dummyAccountFlag);
                batchEntry.setOriginalIssueFlag(0);
                if (statusCode.equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
                    batchEntry.setBox97EntryFlag(TransferTypeUtil.setBox97EntryFlagForOTT(e.getEntryType()));
                    batchEntry.setEntryType(Constants.CLOSE_TYPE_NORMAL);
                } else if (statusCode.equalsIgnoreCase(Constants.STATUS_CLOSED)) {
                    batchEntry.setBox97EntryFlag(box97EntryFlag);
                    batchEntry.setEntryType(TransferTypeUtil.setBatchEntryTypeClose(e.getEntryType()));
                }
                batchEntries.add(batchEntry);
            }
        }
        return batchEntries;
    }

    private Beottc getBatchEntriesForOTTClient(String transferTypeCode) {
        Beottc beottc = new Beottc();
        beottc.setOttEntry(getOTTDetails(transferTypeCode, 0));
        beottc.setInto97Entry(getInto97Details(transferTypeCode, 0));
        return beottc;
    }

    private Beottd getBatchEntriesForOTTDummy(String transferTypeCode) {
        Beottd beottd = new Beottd();
        beottd.setOttEntry(getOTTDetails(transferTypeCode, 1));
        beottd.setInto97Entry(getInto97Details(transferTypeCode, 1));
        return beottd;
    }

    private List<Entry> getInto97Details(String transferTypeCode, Integer dummy) {
        List<Entry> entries = new ArrayList<>();
        Optional<List<BatchEntry>> batchEntries = batchEntryRepository.findByTransferTypeCodeAndStatusCodeAndDummyAccountFlagAndBox97EntryFlag(transferTypeCode,
                Constants.STATUS_OUT_TO_TRANSFER, dummy, 1);
        if (batchEntries.isPresent()) {
            for (int i = 0; i < batchEntries.get().size(); i++) {
                Entry entry = new Entry();
                entry.setId(batchEntries.get().get(i).getBatchEntryId());
                entry.setBatchCode(batchEntries.get().get(i).getBatchCode() != null ? batchEntries.get().get(i).getBatchCode().trim() : null);
                entry.setEntryCode(batchEntries.get().get(i).getEntryCode() != null ? batchEntries.get().get(i).getEntryCode().trim() : null);
                entry.setCse(batchEntries.get().get(i).getDeleteOneCsEntryFlag() == 1);
                entry.setEntryTypeIssueCode(!StringUtils.isBlank(batchEntries.get().get(i).getIssueTypeCode()) ? IssueTypeCode.valueOf(batchEntries.get().get(i).getIssueTypeCode().trim()) : null);
                entry.setUpdateDb(batchEntries.get().get(i).getDbUpdateFlag() == 1);
                entry.setAcctBoxLoc(TransferTypeUtil.formatAcctNbrTypeDgt(batchEntries.get().get(i).getMemoAccountNumber(), batchEntries.get().get(i).getMemoAccountType(), batchEntries.get().get(i).getMemoAccountCheckDigit()));
                entry.setBox97(batchEntries.get().get(i).getBox97EntryFlag() == 1);
                entry.setDummy(batchEntries.get().get(i).getDummyAccountFlag() == 1);
                entry.setOriginalIssue(batchEntries.get().get(i).getOriginalIssueFlag() == 1);
                Map<String, Object> memoType = TransferTypeUtil.extractRecordTypeAndDebitCredit(batchEntries.get().get(i).getMemoType());
                entry.setDebitOrCredit(memoType != null ? (DebitCreditType) memoType.get(Constants.DEBIT_CREDIT) : null);
                entry.setRecordType(memoType != null ? (RecordType) memoType.get(Constants.RECORD_TYPE) : null);

                Map<String, CodeType> shares = TransferTypeUtil.extractSharesFromCode(batchEntries.get().get(i).getSharesFromCode());
                entry.setQty(shares != null ? shares.get(Constants.SH) : null);
                entry.setDollars(shares != null ? shares.get(Constants.CA) : null);
                entry.setSecurity(shares != null ? shares.get(Constants.SEC) : null);
                entry.setEntryType("Into 97 Entry " + (i + 1));
                entry.setEntryTypeOption(batchEntries.get().get(i).getEntryType() != null ? batchEntries.get().get(i).getEntryType().trim() : null);
                entries.add(entry);
            }
        }
        return entries;
    }

    private List<Entry> getOTTDetails(String transferTypeCode, Integer dummy) {
        List<Entry> entries = new ArrayList<>();
        Optional<List<BatchEntry>> batchEntries = batchEntryRepository.findByTransferTypeCodeAndStatusCodeAndDummyAccountFlagAndBox97EntryFlag(transferTypeCode,
                Constants.STATUS_OUT_TO_TRANSFER, dummy, 0);
        if (batchEntries.isPresent()) {
            for (int i = 0; i < batchEntries.get().size(); i++) {
                Entry entry = new Entry();
                entry.setId(batchEntries.get().get(i).getBatchEntryId());
                entry.setBatchCode(batchEntries.get().get(i).getBatchCode() != null ? batchEntries.get().get(i).getBatchCode().trim() : null);
                entry.setEntryCode(batchEntries.get().get(i).getEntryCode() != null ? batchEntries.get().get(i).getEntryCode().trim() : null);
                entry.setCse(batchEntries.get().get(i).getDeleteOneCsEntryFlag() == 1);
                entry.setEntryTypeIssueCode(!StringUtils.isBlank(batchEntries.get().get(i).getIssueTypeCode()) ? IssueTypeCode.valueOf(batchEntries.get().get(i).getIssueTypeCode().trim()) : null);
                entry.setUpdateDb(batchEntries.get().get(i).getDbUpdateFlag() == 1);
                entry.setAcctBoxLoc(TransferTypeUtil.formatAcctNbrTypeDgt(batchEntries.get().get(i).getMemoAccountNumber(), batchEntries.get().get(i).getMemoAccountType(), batchEntries.get().get(i).getMemoAccountCheckDigit()));
                entry.setBox97(batchEntries.get().get(i).getBox97EntryFlag() == 1);
                entry.setDummy(batchEntries.get().get(i).getDummyAccountFlag() == 1);
                entry.setOriginalIssue(batchEntries.get().get(i).getOriginalIssueFlag() == 1);
                Map<String, Object> memoType = TransferTypeUtil.extractRecordTypeAndDebitCredit(batchEntries.get().get(i).getMemoType());
                entry.setDebitOrCredit(memoType != null ? (DebitCreditType) memoType.get(Constants.DEBIT_CREDIT) : null);
                entry.setRecordType(memoType != null ? (RecordType) memoType.get(Constants.RECORD_TYPE) : null);

                Map<String, CodeType> shares = TransferTypeUtil.extractSharesFromCode(batchEntries.get().get(i).getSharesFromCode());
                entry.setQty(shares != null ? shares.get(Constants.SH) : null);
                entry.setDollars(shares != null ? shares.get(Constants.CA) : null);
                entry.setSecurity(shares != null ? shares.get(Constants.SEC) : null);
                entry.setEntryType("OTT Entry " + (i + 1));
                entry.setEntryTypeOption(batchEntries.get().get(i).getEntryType() != null ? batchEntries.get().get(i).getEntryType().trim() : null);
                entries.add(entry);
            }
        }
        return entries;
    }

    private Beclosc getBatchEntriesForCloseClient(String transferTypeCode) {
        Beclosc beclosc = new Beclosc();
        beclosc.setNormalClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_NORMAL, 0, 0));
        beclosc.setEscheatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_ESCHEATED, 0, 0));
        beclosc.setWorthlessClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_WORTHLESS, 0, 0));
        beclosc.setRejectedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_REJECTED, 0, 0));
        beclosc.setConfiscatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_CONFISCATED, 0, 0));
        return beclosc;
    }

    private Beclosd getBatchEntriesForCloseDummy(String transferTypeCode) {
        Beclosd beclosd = new Beclosd();
        beclosd.setNormalClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_NORMAL, 1, 0));
        beclosd.setEscheatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_ESCHEATED, 1, 0));
        beclosd.setWorthlessClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_WORTHLESS, 1, 0));
        beclosd.setRejectedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_REJECTED, 1, 0));
        beclosd.setConfiscatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_CONFISCATED, 1, 0));
        return beclosd;
    }

    private Beclosc97 getBatchEntriesForCloseClient97(String transferTypeCode) {
        Beclosc97 beclosc97 = new Beclosc97();
        beclosc97.setNormalClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_NORMAL, 0, 1));
        beclosc97.setEscheatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_ESCHEATED, 0, 1));
        beclosc97.setWorthlessClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_WORTHLESS, 0, 1));
        beclosc97.setRejectedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_REJECTED, 0, 1));
        beclosc97.setConfiscatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_CONFISCATED, 0, 1));
        return beclosc97;
    }


    private Beclosd97 getBatchEntriesForCloseDummy97(String transferTypeCode) {
        Beclosd97 beclosd97 = new Beclosd97();
        beclosd97.setNormalClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_NORMAL, 1, 1));
        beclosd97.setEscheatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_ESCHEATED, 1, 1));
        beclosd97.setWorthlessClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_WORTHLESS, 1, 1));
        beclosd97.setRejectedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_REJECTED, 1, 1));
        beclosd97.setConfiscatedClose(getBatchEntriesForClose(transferTypeCode, Constants.CLOSE_TYPE_CONFISCATED, 1, 1));
        return beclosd97;
    }

    private List<Entry> getBatchEntriesForClose(String transferTypeCode, String entryType, Integer dummy, Integer box97) {
        List<Entry> entries = new ArrayList<>();
        Optional<List<BatchEntry>> batchEntries = batchEntryRepository.findByTransferTypeCodeAndEntryTypeAndStatusCodeAndDummyAccountFlagAndBox97EntryFlag(transferTypeCode,
                entryType, Constants.STATUS_CLOSED, dummy, box97);
        {
            if (batchEntries.isPresent()) {
                for (int i = 0; i < batchEntries.get().size(); i++) {
                    Entry entry = new Entry();
                    entry.setId(batchEntries.get().get(i).getBatchEntryId());
                    entry.setBatchCode(batchEntries.get().get(i).getBatchCode() != null ? batchEntries.get().get(i).getBatchCode().trim() : null);
                    entry.setEntryCode(batchEntries.get().get(i).getEntryCode() != null ? batchEntries.get().get(i).getEntryCode().trim() : null);
                    entry.setCse(batchEntries.get().get(i).getDeleteOneCsEntryFlag() == 1);
                    entry.setEntryTypeIssueCode(!StringUtils.isBlank(batchEntries.get().get(i).getIssueTypeCode()) ? IssueTypeCode.valueOf(batchEntries.get().get(i).getIssueTypeCode().trim()) : null);
                    entry.setUpdateDb(batchEntries.get().get(i).getDbUpdateFlag() == 1);
                    entry.setAcctBoxLoc(TransferTypeUtil.formatAcctNbrTypeDgt(batchEntries.get().get(i).getMemoAccountNumber(), batchEntries.get().get(i).getMemoAccountType(), batchEntries.get().get(i).getMemoAccountCheckDigit()));
                    entry.setBox97(batchEntries.get().get(i).getBox97EntryFlag() == 1);
                    entry.setDummy(batchEntries.get().get(i).getDummyAccountFlag() == 1);
                    entry.setOriginalIssue(batchEntries.get().get(i).getOriginalIssueFlag() == 1);
                    Map<String, Object> memoType = TransferTypeUtil.extractRecordTypeAndDebitCredit(batchEntries.get().get(i).getMemoType());
                    entry.setDebitOrCredit(memoType != null ? (DebitCreditType) memoType.get(Constants.DEBIT_CREDIT) : null);
                    entry.setRecordType(memoType != null ? (RecordType) memoType.get(Constants.RECORD_TYPE) : null);

                    Map<String, CodeType> shares = TransferTypeUtil.extractSharesFromCode(batchEntries.get().get(i).getSharesFromCode());
                    entry.setQty(shares != null ? shares.get(Constants.SH) : null);
                    entry.setDollars(shares != null ? shares.get(Constants.CA) : null);
                    entry.setSecurity(shares != null ? shares.get(Constants.SEC) : null);
                    if (entryType.equalsIgnoreCase(Constants.CLOSE_TYPE_NORMAL)) {
                        entry.setEntryType("Normal Close " + (i + 1));
                    } else if (entryType.equalsIgnoreCase(Constants.CLOSE_TYPE_ESCHEATED)) {
                        entry.setEntryType("Escheated Close " + (i + 1));
                    } else if (entryType.equalsIgnoreCase(Constants.CLOSE_TYPE_WORTHLESS)) {
                        entry.setEntryType("Worthless Close " + (i + 1));
                    } else if (entryType.equalsIgnoreCase(Constants.CLOSE_TYPE_REJECTED)) {
                        entry.setEntryType("Rejected Close " + (i + 1));
                    } else if (entryType.equalsIgnoreCase(Constants.CLOSE_TYPE_CONFISCATED)) {
                        entry.setEntryType("Confiscated Close " + (i + 1));
                    }
                    entry.setEntryTypeOption(batchEntries.get().get(i).getEntryType());
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    public List<BatchEntry> updateBatchEntries(TransferTypeDTO form) {
        //        updateBatchEntry(form, form.getBatchEntry().getBeottc(), Constants.STATUS_OUT_TO_TRANSFER, 0, null);
        //        updateBatchEntry(form, form.getBatchEntry().getBeottd(), Constants.STATUS_OUT_TO_TRANSFER, 1, null);
        //        updateBatchEntry(form, form.getBatchEntry().getBeclosc(), Constants.STATUS_CLOSED, 0, 0);
        //        updateBatchEntry(form, form.getBatchEntry().getBeclosd(), Constants.STATUS_CLOSED, 1, 0);
        //        updateBatchEntry(form, form.getBatchEntry().getBeclosc97(), Constants.STATUS_CLOSED, 0, 1);
        //        updateBatchEntry(form, form.getBatchEntry().getBeclosd97(), Constants.STATUS_CLOSED, 1, 1);
        List<BatchEntry> batchEntries = new ArrayList<>();
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeottc().getOttEntry(), Constants.STATUS_OUT_TO_TRANSFER, 0, null));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeottc().getInto97Entry(), Constants.STATUS_OUT_TO_TRANSFER, 0, null));

        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeottd().getOttEntry(), Constants.STATUS_OUT_TO_TRANSFER, 1, null));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeottd().getInto97Entry(), Constants.STATUS_OUT_TO_TRANSFER, 1, null));

        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc().getNormalClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc().getEscheatedClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc().getWorthlessClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc().getRejectedClose(), Constants.STATUS_CLOSED, 0, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc().getConfiscatedClose(), Constants.STATUS_CLOSED, 0, 0));

        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd().getNormalClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd().getEscheatedClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd().getWorthlessClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd().getRejectedClose(), Constants.STATUS_CLOSED, 1, 0));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd().getConfiscatedClose(), Constants.STATUS_CLOSED, 1, 0));

        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc97().getNormalClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc97().getEscheatedClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc97().getWorthlessClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc97().getRejectedClose(), Constants.STATUS_CLOSED, 0, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosc97().getConfiscatedClose(), Constants.STATUS_CLOSED, 0, 1));

        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd97().getNormalClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd97().getEscheatedClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd97().getWorthlessClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd97().getRejectedClose(), Constants.STATUS_CLOSED, 1, 1));
        batchEntries.addAll(updateBatchEntry(form, form.getBatchEntry().getBeclosd97().getConfiscatedClose(), Constants.STATUS_CLOSED, 1, 1));
        return batchEntries;
    }

    private List<BatchEntry> updateBatchEntry(TransferTypeDTO form,
                                              List<Entry> entries,
                                              String statusCode,
                                              Integer dummyAccountFlag,
                                              Integer box97EntryFlag) {
        List<BatchEntry> batchEntries = new ArrayList<>();
        if (entries != null && entries.size() > 0) {
            for (Entry e : entries) {
                if (e.getId() != null) {
                    Optional<BatchEntry> batchEntry = batchEntryRepository.findById(e.getId());
                    if (batchEntry.isPresent()) {
                        if (e.getIsDelete()) {
                            batchEntryRepository.deleteById(e.getId());
                        } else {
                            EntryAcctNbr entryAcctNbr = new EntryAcctNbr(e.getAcctBoxLoc());
                            batchEntry.get().setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                            batchEntry.get().setLastUpdateName(form.getLastUpdateName());
                            batchEntry.get().setLastUpdateDt(new Date());
                            batchEntry.get().setEntryCode(e.getEntryCode());
                            batchEntry.get().setBatchCode(e.getBatchCode());
                            batchEntry.get().setStatusCode(statusCode);
                            batchEntry.get().setMemoAccountNumber(entryAcctNbr.getEntryAcctNbr());
                            batchEntry.get().setMemoAccountType(entryAcctNbr.getEntryAcctType());
                            batchEntry.get().setMemoAccountCheckDigit(entryAcctNbr.getEntryAcctCheckDigit());
                            batchEntry.get().setMemoType(TransferTypeUtil.generateMemoType(e.getDebitOrCredit(), e.getRecordType()));
                            batchEntry.get().setSharesFromCode(TransferTypeUtil.generateSharesFromCode(e.getQty(), e.getDollars(), e.getSecurity()));
                            batchEntry.get().setDeleteOneCsEntryFlag(Utility.convertBooleanToInteger(e.getCse()));
                            batchEntry.get().setDbUpdateFlag(Utility.convertBooleanToInteger(e.getUpdateDb()));
                            batchEntry.get().setIssueTypeCode(e.getEntryTypeIssueCode() != null ? e.getEntryTypeIssueCode().toString() : null);
                            batchEntry.get().setDummyAccountFlag(dummyAccountFlag);
                            batchEntry.get().setOriginalIssueFlag(e.getOriginalIssue() ? 1 : 0);
                            if (statusCode.equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
                                batchEntry.get().setBox97EntryFlag(TransferTypeUtil.setBox97EntryFlagForOTT(e.getEntryType()));
                                batchEntry.get().setEntryType(Constants.CLOSE_TYPE_NORMAL);
                            } else if (statusCode.equalsIgnoreCase(Constants.STATUS_CLOSED)) {
                                batchEntry.get().setBox97EntryFlag(box97EntryFlag);
                                batchEntry.get().setEntryType(TransferTypeUtil.setBatchEntryTypeClose(e.getEntryType()));
                            }
                            batchEntries.add(batchEntry.get());
                            //batchEntryRepository.save(batchEntry.get());
                        }
                    }

                } else {
                    BatchEntry batchEntry = new BatchEntry();
                    EntryAcctNbr entryAcctNbr = new EntryAcctNbr(e.getAcctBoxLoc());
                    batchEntry.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                    batchEntry.setLastUpdateName(form.getLastUpdateName());
                    batchEntry.setLastUpdateDt(new Date());
                    batchEntry.setEntryCode(e.getEntryCode());
                    batchEntry.setBatchCode(e.getBatchCode());
                    batchEntry.setStatusCode(statusCode);
                    batchEntry.setMemoAccountNumber(entryAcctNbr.getEntryAcctNbr());
                    batchEntry.setMemoAccountType(entryAcctNbr.getEntryAcctType());
                    batchEntry.setMemoAccountCheckDigit(entryAcctNbr.getEntryAcctCheckDigit());
                    batchEntry.setMemoType(TransferTypeUtil.generateMemoType(e.getDebitOrCredit(), e.getRecordType()));
                    batchEntry.setSharesFromCode(TransferTypeUtil.generateSharesFromCode(e.getQty(), e.getDollars(), e.getSecurity()));
                    batchEntry.setDeleteOneCsEntryFlag(Utility.convertBooleanToInteger(e.getCse()));
                    batchEntry.setDbUpdateFlag(Utility.convertBooleanToInteger(e.getUpdateDb()));
                    batchEntry.setIssueTypeCode(e.getEntryTypeIssueCode() != null ? e.getEntryTypeIssueCode().toString() : null);
                    batchEntry.setDummyAccountFlag(dummyAccountFlag);
                    batchEntry.setOriginalIssueFlag(0);
                    if (statusCode.equalsIgnoreCase(Constants.STATUS_OUT_TO_TRANSFER)) {
                        batchEntry.setBox97EntryFlag(TransferTypeUtil.setBox97EntryFlagForOTT(e.getEntryType()));
                        batchEntry.setEntryType(Constants.CLOSE_TYPE_NORMAL);
                    } else if (statusCode.equalsIgnoreCase(Constants.STATUS_CLOSED)) {
                        batchEntry.setBox97EntryFlag(box97EntryFlag);
                        batchEntry.setEntryType(TransferTypeUtil.setBatchEntryTypeClose(e.getEntryType()));
                    }
                    batchEntries.add(batchEntry);
                    //batchEntryRepository.save(batchEntry.get());
                }
            }
        }
        return batchEntries;
    }
}
