package com.rbc.zfe0.road.services.transferitem.dao;

import com.rbc.zfe0.road.services.transferitem.dto.EntryAcctNbr;
import com.rbc.zfe0.road.services.transferitem.entity.Issue;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import com.rbc.zfe0.road.services.transferitem.model.EditTransferItem;
import com.rbc.zfe0.road.services.transferitem.model.NewIssueCash;
import com.rbc.zfe0.road.services.transferitem.model.NewIssueSecurities;
import com.rbc.zfe0.road.services.transferitem.repository.IssueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class IssueDao {
    @Autowired
    IssueRepository issueRepository;

    public Issue saveNewIssueSecurities(Integer issueId, EditTransferItem editTransferItem) {

        List<NewIssueSecurities> newIssueSecuritiesList = editTransferItem.getNewIssueSecurities();
        if (newIssueSecuritiesList == null || newIssueSecuritiesList.isEmpty()) {
// handle the scenario when the newIssueCashList is empty
// return null or throw an exception depending on requirements
            return null;
        }

        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found"));

        for (NewIssueSecurities newSecurity : editTransferItem.getNewIssueSecurities()) {
            if (newSecurity.getIssueId().equals(issueId)) {
                String debitLong = newSecurity.getNewDebitValue();
                String credLong = newSecurity.getNewCredValue();
                if (newSecurity.getIssueId().equals(issueId)) {
// check if the transferItemId exists in the database
                    Optional<Issue> existingIssue = issueRepository.findById(issueId);
                    if (existingIssue.get().getTransferItem().getTransferItemId().equals(editTransferItem.getTransferItemId())) {
                        issue.getTransferItem().setTransferItemId(editTransferItem.getTransferItemId());
                        issue.setCusip(newSecurity.getNewCusip());
                        issue.setAdpSecurityNumber(newSecurity.getNewSecurity());
                        issue.setSecurityDescription(newSecurity.getNewSecDesc());
                        issue.setQuantity(newSecurity.getNewQuantity());
                        issue.setCertificateNumber(newSecurity.getNewCart());
                        issue.setCertificateDate(new Date());
                        issue.setInsuranceValue(convertAmount(newSecurity.getNewInsurance()));
                        issue.setDenomination(newSecurity.getNewDenomination());
                        setNewIssueSecurityDebitLong(debitLong, issue);
                        setNewIssueSecurityCreditLong(credLong, issue);
                        issue.setBatchCode(newSecurity.getNewBatchCode());
                        issue.setEntryCode(newSecurity.getNewEntryCode());
                    }
                }
            }
        }

        return issueRepository.save(issue);
    }




    public Issue saveNewIssueCash(Integer issueId, EditTransferItem editTransferItem) {

        List<NewIssueCash> newIssueCashList = editTransferItem.getNewIssueCash();
        if (newIssueCashList == null || newIssueCashList.isEmpty()) {
// handle the scenario when the newIssueCashList is empty
// return null or throw an exception depending on  requirements
            return null;
        }

        Issue issue = new Issue();
        issue.setCloseFlag(0);

        for (NewIssueCash newIssue : newIssueCashList) {
            String debitLong = newIssue.getDebitLong();
            String credLong = newIssue.getCreditShort();
            if (newIssue.getIssueId().equals(issueId)) {
// check if the issueId exists in the database
                Optional<Issue> existingIssue = issueRepository.findById(issueId);
                if (existingIssue.isPresent()) {
                    issue = existingIssue.get();
                    setNewIssueCashDebitLong(debitLong, issue);
                    setNewIssueCashCreditLong(credLong, issue);
                    issue.setInsuranceValue(convertAmount(newIssue.getAmtRequested()));
                    issue.setLastUpdateDt(newIssue.getNewIssueRecd());
                    issue.setCheckNumber(newIssue.getCheckOrWire());
                    issue.setEntryDate(new Date());
                    issue.setLastUpdateDt(new Date());
                }

            }

        }
        return issueRepository.save(issue);
    }


    public BigDecimal convertAmount(BigDecimal value) {
        BigDecimal amount = value.stripTrailingZeros();
        return amount;
    }

    public String convertAmount(String value) {
        String amount = value.replace(",", "");
        return amount;
    }


    public void setNewIssueCashDebitLong(String debitLong, Issue issue) {
// Remove all '-' characters from the original account number
        String debitLongNumber = debitLong.replaceAll("-", "");

// Extract the first 8 digits of the account number
        String dlEnterAccNumber = debitLongNumber.substring(0, 8);

// Extract the account type (9th digit)
        String dleAccType = debitLongNumber.substring(8, 9);

// Extract the check digit (10th digit)
        String dleAccCheckDigit = debitLongNumber.substring(9, 10);

        issue.setDleAccountNumber(dlEnterAccNumber);
        issue.setDleAccountType(dleAccType);
        issue.setDleAccountCheckDigit(dleAccCheckDigit);

    }
    public void setNewIssueCashCreditLong(String creditLong, Issue issue) {
// Remove all '-' characters from the original account number
        String creditLongNumber = creditLong.replaceAll("-", "");

// Extract the first 8 digits of the account number
        String crEnterAccNumber = creditLongNumber.substring(0, 8);

// Extract the account type (9th digit)
        String creAccType = creditLongNumber.substring(8, 9);

// Extract the check digit (10th digit)
        String creAccCheckDigit = creditLongNumber.substring(9, 10);

        issue.setCseAccountNumber(crEnterAccNumber);
        issue.setCseAccountType(creAccType);
        issue.setCseAccountCheckDigit(creAccCheckDigit);

    }

    private void setNewIssueSecurityCreditLong(String creditLong, Issue issue) {
// Remove all '-' characters from the original account number
        String creditLongNumber = creditLong.replaceAll("-", "");

// Extract the first 8 digits of the account number
        String crEnterAccNumber = creditLongNumber.substring(0, 8);

// Extract the account type (9th digit)
        String creAccType = creditLongNumber.substring(8, 9);

// Extract the check digit (10th digit)
        String creAccCheckDigit = creditLongNumber.substring(9, 10);

        issue.setCseAccountNumber(crEnterAccNumber);
        issue.setCseAccountType(creAccType);
        issue.setCseAccountCheckDigit(creAccCheckDigit);
    }

    private void setNewIssueSecurityDebitLong(String debitLong, Issue issue) {
// Remove all '-' characters from the original account number
        String debitLongNumber = debitLong.replaceAll("-", "");

// Extract the first 8 digits of the account number
        String dlEnterAccNumber = debitLongNumber.substring(0, 8);

// Extract the account type (9th digit)
        String dleAccType = debitLongNumber.substring(8, 9);

// Extract the check digit (10th digit)
        String dleAccCheckDigit = debitLongNumber.substring(9, 10);

        issue.setDleAccountNumber(dlEnterAccNumber);
        issue.setDleAccountType(dleAccType);
        issue.setDleAccountCheckDigit(dleAccCheckDigit);
    }

    public Issue saveSecurity(NewIssueSecurities issueSecurity, TransferItem transferItem, Date currentDate, String user) {
        if (issueSecurity != null && issueSecurity.getIssueId() != null) {
            Optional<Issue> issue = issueRepository.findById(issueSecurity.getIssueId());
            if (issue.isPresent()) {
                issue.get().setTransferItem(transferItem);
                issue.get().setAdpSecurityNumber(issueSecurity.getNewSecurity());
                issue.get().setCashEntryFlag(0);
                issue.get().setCusip(issueSecurity.getNewCusip());
                issue.get().setSecurityDescription(issueSecurity.getNewSecDesc());
                issue.get().setQuantity(issueSecurity.getNewQuantity());
                issue.get().setCertificateNumber(issueSecurity.getNewCart());
                issue.get().setCertificateDate(issueSecurity.getNewCartDate());
                issue.get().setDenomination(issueSecurity.getNewDenomination());
                issue.get().setInsuranceValue(issueSecurity.getNewInsurance());
                issue.get().setCloseFlag(0);
                EntryAcctNbr debit = new EntryAcctNbr(issueSecurity.getNewDebitValue());
                issue.get().setDleAccountNumber(debit.getEntryAcctNbr());
                issue.get().setDleAccountType(debit.getEntryAcctType());
                issue.get().setDleAccountCheckDigit(debit.getEntryAcctCheckDigit());

                EntryAcctNbr credit = new EntryAcctNbr(issueSecurity.getNewDebitValue());
                issue.get().setCseAccountNumber(credit.getEntryAcctNbr());
                issue.get().setCseAccountType(credit.getEntryAcctType());
                issue.get().setCseAccountCheckDigit(credit.getEntryAcctCheckDigit());

                issue.get().setBatchCode(issueSecurity.getNewBatchCode());
                issue.get().setEntryCode(issueSecurity.getNewEntryCode());
                issue.get().setLastUpdateDt(currentDate);
                issue.get().setLastUpdateName(user);
            }
            return issue.get();
        } else {
            Issue issue = new Issue();
            issue.setTransferItem(transferItem);
            issue.setAdpSecurityNumber(issueSecurity.getNewSecurity());
            issue.setCashEntryFlag(0);
            issue.setCusip(issueSecurity.getNewCusip());
            issue.setSecurityDescription(issueSecurity.getNewSecDesc());
            issue.setQuantity(issueSecurity.getNewQuantity());
            issue.setCertificateNumber(issueSecurity.getNewCart());
            issue.setCertificateDate(issueSecurity.getNewCartDate());
            issue.setDenomination(issueSecurity.getNewDenomination());
            issue.setInsuranceValue(issueSecurity.getNewInsurance());
            issue.setBatchCode(issueSecurity.getNewBatchCode());
            issue.setEntryCode(issueSecurity.getNewEntryCode());
            issue.setLastUpdateDt(currentDate);
            issue.setLastUpdateName(user);
            issue.setCloseFlag(0);
            issue.setBlockFlag(0);
            EntryAcctNbr debit = new EntryAcctNbr(issueSecurity.getNewDebitValue());
            issue.setDleAccountNumber(debit.getEntryAcctNbr());
            issue.setDleAccountType(debit.getEntryAcctType());
            issue.setDleAccountCheckDigit(debit.getEntryAcctCheckDigit());

            EntryAcctNbr credit = new EntryAcctNbr(issueSecurity.getNewDebitValue());
            issue.setCseAccountNumber(credit.getEntryAcctNbr());
            issue.setCseAccountType(credit.getEntryAcctType());
            issue.setCseAccountCheckDigit(credit.getEntryAcctCheckDigit());
            return issue;
        }
    }

    public Issue saveCash(NewIssueCash issueCash, TransferItem transferItem, Date currentDate, String user) {
        if (issueCash != null && issueCash.getIssueId() != null) {
            Optional<Issue> issue = issueRepository.findById(issueCash.getIssueId());
            if(issue.isPresent()) {
                issue.get().setTransferItem(transferItem);
                issue.get().setInsuranceValue(issueCash.getAmtRequested());
                issue.get().setCheckAmount(issueCash.getAmtReceived());
                issue.get().setCheckNumber(issueCash.getCheckOrWire());
                issue.get().setBatchCode(issueCash.getBatchCode());
                issue.get().setEntryCode(issueCash.getEntryCode());
                issue.get().setEntryDate(issueCash.getNewIssueRecd());
                issue.get().setBatchCode(issueCash.getBatchCode());
                issue.get().setSecurityDescription("CASH");
                issue.get().setLastUpdateDt(currentDate);
                issue.get().setLastUpdateName(user);
                issue.get().setCloseFlag(0);
                EntryAcctNbr debit = new EntryAcctNbr(issueCash.getDebitLong());
                issue.get().setDleAccountNumber(debit.getEntryAcctNbr());
                issue.get().setDleAccountType(debit.getEntryAcctType());
                issue.get().setDleAccountCheckDigit(debit.getEntryAcctCheckDigit());

                EntryAcctNbr credit = new EntryAcctNbr(issueCash.getCreditShort());
                issue.get().setCseAccountNumber(credit.getEntryAcctNbr());
                issue.get().setCseAccountType(credit.getEntryAcctType());
                issue.get().setCseAccountCheckDigit(credit.getEntryAcctCheckDigit());
            }
            return issue.get();
        } else {
            Issue issue = new Issue();
            issue.setTransferItem(transferItem);
            issue.setCashEntryFlag(1);
            issue.setInsuranceValue(issueCash.getAmtRequested());
            issue.setCheckAmount(issueCash.getAmtReceived());
            issue.setCheckNumber(issueCash.getCheckOrWire());
            issue.setBatchCode(issueCash.getBatchCode());
            issue.setEntryCode(issueCash.getEntryCode());
            issue.setEntryDate(issueCash.getNewIssueRecd());
            issue.setBatchCode(issueCash.getBatchCode());
            issue.setSecurityDescription("CASH");
            issue.setLastUpdateDt(currentDate);
            issue.setLastUpdateName(user);
            issue.setCloseFlag(0);
            issue.setBlockFlag(0);

            EntryAcctNbr debit = new EntryAcctNbr(issueCash.getDebitLong());
            issue.setDleAccountNumber(debit.getEntryAcctNbr());
            issue.setDleAccountType(debit.getEntryAcctType());
            issue.setDleAccountCheckDigit(debit.getEntryAcctCheckDigit());

            EntryAcctNbr credit = new EntryAcctNbr(issueCash.getCreditShort());
            issue.setCseAccountNumber(credit.getEntryAcctNbr());
            issue.setCseAccountType(credit.getEntryAcctType());
            issue.setCseAccountCheckDigit(credit.getEntryAcctCheckDigit());
            return issue;
        }
    }

    public Issue receiveCash(NewIssueCash issueCash, TransferItem transferItem, Date currentDate, String user) {
        Optional<Issue> issue = issueRepository.findById(issueCash.getIssueId());
        if(issue.isPresent()) {
            issue.get().setTransferItem(transferItem);
            issue.get().setInsuranceValue(issueCash.getAmtRequested());
            issue.get().setCheckAmount(issueCash.getAmtReceived());
            issue.get().setCheckNumber(issueCash.getCheckOrWire());
            issue.get().setBatchCode(issueCash.getBatchCode());
            issue.get().setEntryCode(issueCash.getEntryCode());
            issue.get().setEntryDate(issueCash.getNewIssueRecd());
            issue.get().setBatchCode(issueCash.getBatchCode());
            issue.get().setSecurityDescription("CASH");
            issue.get().setLastUpdateDt(currentDate);
            issue.get().setLastUpdateName(user);
            issue.get().setCloseFlag(0);
            EntryAcctNbr debit = new EntryAcctNbr(issueCash.getDebitLong());
            issue.get().setDleAccountNumber(debit.getEntryAcctNbr());
            issue.get().setDleAccountType(debit.getEntryAcctType());
            issue.get().setDleAccountCheckDigit(debit.getEntryAcctCheckDigit());

            EntryAcctNbr credit = new EntryAcctNbr(issueCash.getCreditShort());
            issue.get().setCseAccountNumber(credit.getEntryAcctNbr());
            issue.get().setCseAccountType(credit.getEntryAcctType());
            issue.get().setCseAccountCheckDigit(credit.getEntryAcctCheckDigit());
        }
        return issue.get();
    }

    public Issue receiveSecurity(NewIssueSecurities issueSecurity, TransferItem transferItem, Date currentDate, String user) {
        Optional<Issue> issue = issueRepository.findById(issueSecurity.getIssueId());
        if (issue.isPresent()) {
            issue.get().setTransferItem(transferItem);
            issue.get().setAdpSecurityNumber(issueSecurity.getNewSecurity());
            issue.get().setCashEntryFlag(0);
            issue.get().setCusip(issueSecurity.getNewCusip());
            issue.get().setSecurityDescription(issueSecurity.getNewSecDesc());
            issue.get().setQuantity(issueSecurity.getNewQuantity());
            issue.get().setCertificateNumber(issueSecurity.getNewCart());
            issue.get().setCertificateDate(issueSecurity.getNewCartDate());
            issue.get().setDenomination(issueSecurity.getNewDenomination());
            issue.get().setInsuranceValue(issueSecurity.getNewInsurance());
            issue.get().setEntryDate(currentDate);
            issue.get().setCloseFlag(0);
            EntryAcctNbr debit = new EntryAcctNbr(issueSecurity.getNewDebitValue());
            issue.get().setDleAccountNumber(debit.getEntryAcctNbr());
            issue.get().setDleAccountType(debit.getEntryAcctType());
            issue.get().setDleAccountCheckDigit(debit.getEntryAcctCheckDigit());

            EntryAcctNbr credit = new EntryAcctNbr(issueSecurity.getNewDebitValue());
            issue.get().setCseAccountNumber(credit.getEntryAcctNbr());
            issue.get().setCseAccountType(credit.getEntryAcctType());
            issue.get().setCseAccountCheckDigit(credit.getEntryAcctCheckDigit());

            issue.get().setBatchCode(issueSecurity.getNewBatchCode());
            issue.get().setEntryCode(issueSecurity.getNewEntryCode());
            issue.get().setLastUpdateDt(currentDate);
            issue.get().setLastUpdateName(user);
        }
        return issue.get();
    }

}