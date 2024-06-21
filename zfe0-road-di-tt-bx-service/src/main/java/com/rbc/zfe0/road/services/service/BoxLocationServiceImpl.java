package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.boxlocation.BoxLocationRequest;
import com.rbc.zfe0.road.services.dto.boxlocation.BoxLocationResponse;
import com.rbc.zfe0.road.services.dto.enumtype.CodeType;
import com.rbc.zfe0.road.services.dto.enumtype.DebitCreditType;
import com.rbc.zfe0.road.services.dto.enumtype.RecordType;
import com.rbc.zfe0.road.services.entity.BatchEntry;
import com.rbc.zfe0.road.services.entity.Issue;
import com.rbc.zfe0.road.services.exception.BoxLocationException;
import com.rbc.zfe0.road.services.repository.BatchEntryRepository;
import com.rbc.zfe0.road.services.repository.IssueRepository;
import com.rbc.zfe0.road.services.utils.TransferTypeUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class BoxLocationServiceImpl implements BoxLocationService {

    @Autowired
    private BatchEntryRepository batchEntryRepository;
    @Autowired
    private IssueRepository issueRepository;
    @Override
    public List<BoxLocationResponse> getAllBoxLocations() {
        Optional<List<BatchEntry>> batchEntries = batchEntryRepository.findByTransferTypeCode(Constants.TRANSFER_TYPE_BOX_LOCATION);
        List<BoxLocationResponse> responses = new ArrayList<>();
        batchEntries.ifPresent(bkEntries -> bkEntries.forEach(batchEntry -> {
            BoxLocationResponse response = new BoxLocationResponse();
            response.setBoxLocationId(batchEntry.getBatchEntryId());
            response.setTransferTypeCode(batchEntry.getTransferTypeCode());
            response.setUserChoice(batchEntry.getStatusCode() != null && batchEntry.getStatusCode().equalsIgnoreCase(Constants.BATCH_ENTRY_USER_CHOICE_VALUE));
            response.setBatchCode(batchEntry.getBatchCode());
            response.setEntryCode(batchEntry.getEntryCode());
            response.setEntryType(batchEntry.getEntryType());
            response.setMemoAccountNumber(batchEntry.getMemoAccountNumber() != null ? StringUtils.leftPad(batchEntry.getMemoAccountNumber().trim(), 8, '0') : null);
            response.setMemoAccountCheckDigit(batchEntry.getMemoAccountCheckDigit());
            response.setMemoAccountType(batchEntry.getMemoAccountType());
            response.setBox97EntryFlag(TransferTypeUtil.convertIntToBoolean(batchEntry.getBox97EntryFlag()));
            response.setDbUpdateFlag(TransferTypeUtil.convertIntToBoolean(batchEntry.getDbUpdateFlag()));
            response.setDeleteOneCsEntryFlag(TransferTypeUtil.convertIntToBoolean(batchEntry.getDeleteOneCsEntryFlag()));
            response.setOriginalIssueFlag(TransferTypeUtil.convertIntToBoolean(batchEntry.getOriginalIssueFlag()));
            response.setDummyAccountFlag(TransferTypeUtil.convertIntToBoolean(batchEntry.getDummyAccountFlag()));
            response.setLastUpdateName(batchEntry.getLastUpdateName());
            response.setLastUpdateDt(batchEntry.getLastUpdateDt());
            response.setMapToClientAcct(mapToClientAcct(batchEntry.getEntryType()));
            response.setBoxNumber(formatBoxNumber(batchEntry.getMemoAccountNumber(), batchEntry.getMemoAccountType(), batchEntry.getMemoAccountCheckDigit()));

            Map<String, Object> memoType = TransferTypeUtil.extractRecordTypeAndDebitCredit(batchEntry.getMemoType());
            response.setDebitOrCredit(memoType != null ? (DebitCreditType) memoType.get(Constants.DEBIT_CREDIT) : null);
            response.setRecordType(memoType != null ? (RecordType) memoType.get(Constants.RECORD_TYPE) : null);

            Map<String, CodeType> shares = TransferTypeUtil.extractSharesFromCode(batchEntry.getSharesFromCode());
            response.setQty(shares != null ? shares.get(Constants.SH) : null);
            response.setDollars(shares != null ? shares.get(Constants.CA) : null);
            response.setSecurity(shares != null ? shares.get(Constants.SEC) : null);

            responses.add(response);
        }));
        return responses;
    }

    @Override

    public void createBoxLocation(BoxLocationRequest boxLocReq) {
        BatchEntry batchEntry = new BatchEntry();
        batchEntry.setTransferTypeCode(Constants.TRANSFER_TYPE_BOX_LOCATION);
        batchEntry.setEntryCode(boxLocReq.getEntryCode());
        batchEntry.setBatchCode(boxLocReq.getBatchCode());
        batchEntry.setMemoAccountNumber(boxLocReq.getMemoAccountNumber());
        batchEntry.setMemoAccountType(boxLocReq.getMemoAccountType());
        batchEntry.setMemoAccountCheckDigit(boxLocReq.getMemoAccountCheckDigit());
        batchEntry.setDbUpdateFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getDbUpdateFlag()));
        batchEntry.setDeleteOneCsEntryFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getDeleteOneCsEntryFlag()));
        batchEntry.setOriginalIssueFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getOriginalIssueFlag()));
        batchEntry.setSharesFromCode(TransferTypeUtil.generateSharesFromCode(boxLocReq.getQty(), boxLocReq.getDollars(), boxLocReq.getSecurity()));
        batchEntry.setMemoType(TransferTypeUtil.generateMemoType(boxLocReq.getDebitOrCredit(), boxLocReq.getRecordType()));
        batchEntry.setDummyAccountFlag(0);
        batchEntry.setBox97EntryFlag(0);
        batchEntry.setLastUpdateName(boxLocReq.getLastUpdateName());

        if (boxLocReq.getMapToClientAcct()) {
            batchEntry.setEntryType(Constants.ENTRY_TYPE_C);
        } else {
            batchEntry.setEntryType(null);
        }

        if (boxLocReq.getUserChoice()) {
            batchEntry.setStatusCode(Constants.BATCH_ENTRY_USER_CHOICE_VALUE);
        } else {
            batchEntry.setStatusCode(null);
        }
        batchEntryRepository.save(batchEntry);
        log.info("Box Location : {}", batchEntry);
    }
@Override
    public void updateBoxLocation(Integer boxId, BoxLocationRequest boxLocReq) {
        if (boxId != null) {
            Optional<BatchEntry> batchEntry = batchEntryRepository.findById(boxId);
            if (batchEntry.isPresent()) {
                if (batchEntry.get().getTransferTypeCode().equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION)) {
                    batchEntry.get().setEntryCode(boxLocReq.getEntryCode());
                    batchEntry.get().setBatchCode(boxLocReq.getBatchCode());
                    batchEntry.get().setEntryType(boxLocReq.getEntryType());
                    batchEntry.get().setMemoAccountNumber(boxLocReq.getMemoAccountNumber());
                    batchEntry.get().setMemoAccountType(boxLocReq.getMemoAccountType());
                    batchEntry.get().setMemoAccountCheckDigit(boxLocReq.getMemoAccountCheckDigit());
                    batchEntry.get().setBox97EntryFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getBox97EntryFlag()));
                    batchEntry.get().setDbUpdateFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getDbUpdateFlag()));
                    batchEntry.get().setDeleteOneCsEntryFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getDeleteOneCsEntryFlag()));
                    batchEntry.get().setDummyAccountFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getDummyAccountFlag()));
                    batchEntry.get().setOriginalIssueFlag(TransferTypeUtil.convertBooleanToInt(boxLocReq.getOriginalIssueFlag()));
                    batchEntry.get().setSharesFromCode(TransferTypeUtil.generateSharesFromCode(boxLocReq.getQty(), boxLocReq.getDollars(), boxLocReq.getSecurity()));
                    batchEntry.get().setMemoType(TransferTypeUtil.generateMemoType(boxLocReq.getDebitOrCredit(), boxLocReq.getRecordType()));
                    batchEntry.get().setLastUpdateName(boxLocReq.getLastUpdateName());
                    if (boxLocReq.getMapToClientAcct()) {
                        batchEntry.get().setEntryType(Constants.ENTRY_TYPE_C);
                    } else {
                        batchEntry.get().setEntryType(null);
                    }
                    if (boxLocReq.getUserChoice()) {
                        batchEntry.get().setStatusCode(Constants.BATCH_ENTRY_USER_CHOICE_VALUE);
                    } else {
                        batchEntry.get().setStatusCode(null);
                    }
                    batchEntryRepository.save(batchEntry.get());
                    log.info("Box Location : {}", batchEntry.get());

                } else {
                    throw new BoxLocationException("Box Location not found.");
                }
            }
        } else {
            throw new BoxLocationException("Box Location not found.");
        }
    }
@Override
    public Map<String, String> deleteByBoxLocationId(Integer boxLocationId) {
        Map<String, String> message = new HashMap<>();

        Optional<BatchEntry> boxLoc = batchEntryRepository.findById(boxLocationId);
        if (boxLoc.isPresent()) {
            if (boxLoc.get().getTransferTypeCode().equalsIgnoreCase(Constants.TRANSFER_TYPE_BOX_LOCATION)) {
                String memoAcctNo = paddingMemoAcctNo(boxLoc.get().getMemoAccountNumber() != null ? boxLoc.get().getMemoAccountNumber().trim() : null);
                List<Issue> issues = issueRepository.findByBoxLocationInUse(memoAcctNo, boxLoc.get().getMemoAccountType(), boxLoc.get().getMemoAccountCheckDigit());
                List<String> ti = new ArrayList<>();
                if (issues.size() > 0) {
                    issues.stream().limit(5).forEach(i -> {
                        ti.add(i.getTransferItem().getTransferItemId().toString());
                    });
                    if (issues.size() < 5) {
                        message.put(Constants.Exist, "Box location: " + memoAcctNo + "-" + boxLoc.get().getMemoAccountType() + "-" + boxLoc.get().getMemoAccountCheckDigit() + " - cannot be deleted. It is in use by item(s) #: " + ti);
                    } else {
                        message.put(Constants.Exist, "Box location: " + memoAcctNo + "-" + boxLoc.get().getMemoAccountType() + "-" + boxLoc.get().getMemoAccountCheckDigit() + " - cannot be deleted. It is in use by item(s) #: " + ti + " (first 5 items)");
                    }

                } else {
                    batchEntryRepository.deleteById(boxLoc.get().getBatchEntryId());
                    message.put(Constants.Exist, "The selected box location type is deleted successfully.");
                }
            } else {
                message.put(Constants.NotExist, "The selected box location type does not exist.");
            }
        } else {
            message.put(Constants.NotExist, "The selected box location type does not exist.");
        }
        return message;

    }

    private String paddingMemoAcctNo(String acctNo) {
        String retVal = null;
        if (!StringUtils.isBlank(acctNo)) {
            retVal = StringUtils.leftPad(acctNo.trim(), 8, "0");
        }
        return retVal;
    }

    private Boolean mapToClientAcct(String entryType) {
        if (!StringUtils.isBlank(entryType) && entryType.trim().equalsIgnoreCase(Constants.ENTRY_TYPE_C)) {
            return true;
        } else {
            return false;
        }
    }

    private String formatBoxNumber(String acctNo, String acctType, String acctChkDgt) {
        String retVal = "";
        if (!StringUtils.isBlank(acctNo)) {
            retVal += StringUtils.leftPad(acctNo.trim(), 8, '0') + "-";
            retVal = retVal.substring(0, 3) + "-" + retVal.substring(3);
        }
        if (!StringUtils.isBlank(acctType)) {
            retVal += acctType + "-";
        }
        if (!StringUtils.isBlank(acctChkDgt)) {
            retVal += acctChkDgt;
        }
        return retVal;
    }
}