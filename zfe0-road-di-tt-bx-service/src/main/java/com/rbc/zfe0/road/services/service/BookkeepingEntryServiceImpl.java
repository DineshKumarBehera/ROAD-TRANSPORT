package com.rbc.zfe0.road.services.service;

import com.rbc.zfe0.road.services.constants.Constants;
import com.rbc.zfe0.road.services.dto.entry.EntryAcctNbr;
import com.rbc.zfe0.road.services.dto.transfertype.BookkeepingDTO;
import com.rbc.zfe0.road.services.dto.transfertype.Cashbk;
import com.rbc.zfe0.road.services.dto.transfertype.Secbk;
import com.rbc.zfe0.road.services.dto.transfertype.TransferTypeDTO;
import com.rbc.zfe0.road.services.entity.BookkeepingEntry;
import com.rbc.zfe0.road.services.repository.BookkeepingEntryRepository;
import com.rbc.zfe0.road.services.utils.TransferTypeUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class BookkeepingEntryServiceImpl implements BookkeepingEntryService {

    @Autowired
    BookkeepingEntryRepository bookkeepingEntryRepository;

    @Override
    public List<BookkeepingEntry> addBookkeepingEntries(TransferTypeDTO form) {
        List<BookkeepingEntry> bookkeepingEntries = new ArrayList<>();
        bookkeepingEntries.addAll(addCashBkEntries(form));
        bookkeepingEntries.addAll(addSecurityBkEntries(form));
        return bookkeepingEntries;
    }


    private List<BookkeepingEntry> addCashBkEntries(TransferTypeDTO form) {
        List<BookkeepingEntry> bookkeepingEntries = new ArrayList<>();
        if (form.getBookkeeping() != null && form.getBookkeeping().getCashbk() != null && !form.getBookkeeping().getCashbk().isEmpty() && form.getBookkeeping().getCashbk().size() > 0) {
            for (Cashbk cashbk : form.getBookkeeping().getCashbk()) {
                BookkeepingEntry bkEntry = new BookkeepingEntry();
                bkEntry.setIssueTypeCode(Constants.ISSUE_TYPE_CASH);
                bkEntry.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                bkEntry.setCloseTypeCode(cashbk.getCloseType());

                EntryAcctNbr ottDebitLongAcctNbr = new EntryAcctNbr(cashbk.getOttDebitLong());
                bkEntry.setCloseTypeCode(cashbk.getCloseType());
                bkEntry.setOttDleAccountNumber(ottDebitLongAcctNbr.getEntryAcctNbr());
                bkEntry.setOttDleAccountType(ottDebitLongAcctNbr.getEntryAcctType());
                bkEntry.setOttDleAccountCheckDigit(ottDebitLongAcctNbr.getEntryAcctCheckDigit());

                EntryAcctNbr ottCreditShortAcctNbr = new EntryAcctNbr(cashbk.getOttCreditShort());
                bkEntry.setOttCseAccountNumber(ottCreditShortAcctNbr.getEntryAcctNbr());
                bkEntry.setOttCseAccountType(ottCreditShortAcctNbr.getEntryAcctType());
                bkEntry.setOttCseAccountCheckDigit(ottCreditShortAcctNbr.getEntryAcctCheckDigit());

                EntryAcctNbr closedDebitLongAcctNbr = new EntryAcctNbr(cashbk.getClosedDebitLong());
                bkEntry.setCloseDleAccountNumber(closedDebitLongAcctNbr.getEntryAcctNbr());
                bkEntry.setCloseDleAccountType(closedDebitLongAcctNbr.getEntryAcctType());
                bkEntry.setCloseDleAccountCheckDigit(closedDebitLongAcctNbr.getEntryAcctCheckDigit());

                EntryAcctNbr closedCreditShortAcctNbr = new EntryAcctNbr(cashbk.getClosedCreditShort());
                bkEntry.setCloseCseAccountNumber(closedCreditShortAcctNbr.getEntryAcctNbr());
                bkEntry.setCloseCseAccountType(closedCreditShortAcctNbr.getEntryAcctType());
                bkEntry.setCloseCseAccountCheckDigit(closedCreditShortAcctNbr.getEntryAcctCheckDigit());

                bkEntry.setLastUpdateName(form.getLastUpdateName());
                bkEntry.setLastUpdateDt(new Date());
                bookkeepingEntries.add(bkEntry);
            }
        }
        return bookkeepingEntries;
    }

    private List<BookkeepingEntry> addSecurityBkEntries(TransferTypeDTO form) {
        List<BookkeepingEntry> bookkeepingEntries = new ArrayList<>();
        if (form.getBookkeeping() != null && form.getBookkeeping().getSecbk() != null && !form.getBookkeeping().getSecbk().isEmpty() && form.getBookkeeping().getSecbk().size() > 0) {
            for (Secbk secbk : form.getBookkeeping().getSecbk()) {
                BookkeepingEntry bkEntry = new BookkeepingEntry();
                bkEntry.setIssueTypeCode(Constants.ISSUE_TYPE_SECURITY);
                bkEntry.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                bkEntry.setCloseTypeCode(secbk.getCloseType());
                bkEntry.setLastUpdateName(form.getLastUpdateName());
                bkEntry.setLastUpdateDt(new Date());

                EntryAcctNbr ottDebitLongAcctNbr = new EntryAcctNbr(secbk.getOttDebitLong());
                bkEntry.setOttDleAccountNumber(ottDebitLongAcctNbr.getEntryAcctNbr());
                bkEntry.setOttDleAccountType(ottDebitLongAcctNbr.getEntryAcctType());
                bkEntry.setOttDleAccountCheckDigit(ottDebitLongAcctNbr.getEntryAcctCheckDigit());

                EntryAcctNbr ottCreditShortAcctNbr = new EntryAcctNbr(secbk.getOttCreditShort());
                bkEntry.setOttCseAccountNumber(ottCreditShortAcctNbr.getEntryAcctNbr());
                bkEntry.setOttCseAccountType(ottCreditShortAcctNbr.getEntryAcctType());
                bkEntry.setOttCseAccountCheckDigit(ottCreditShortAcctNbr.getEntryAcctCheckDigit());

                EntryAcctNbr closedDebitLongAcctNbr = new EntryAcctNbr(secbk.getClosedDebitLong());
                bkEntry.setCloseDleAccountNumber(closedDebitLongAcctNbr.getEntryAcctNbr());
                bkEntry.setCloseDleAccountType(closedDebitLongAcctNbr.getEntryAcctType());
                bkEntry.setCloseDleAccountCheckDigit(closedDebitLongAcctNbr.getEntryAcctCheckDigit());

                EntryAcctNbr closedCreditShortAcctNbr = new EntryAcctNbr(secbk.getClosedCreditShort());
                bkEntry.setCloseCseAccountNumber(closedCreditShortAcctNbr.getEntryAcctNbr());
                bkEntry.setCloseCseAccountType(closedCreditShortAcctNbr.getEntryAcctType());
                bkEntry.setCloseCseAccountCheckDigit(closedCreditShortAcctNbr.getEntryAcctCheckDigit());

                bookkeepingEntries.add(bkEntry);
            }
        }
        return bookkeepingEntries;
    }

    public BookkeepingDTO getBookkeepingEntriesByCode(String ttCode) {
        Optional<List<BookkeepingEntry>> bkEntries = bookkeepingEntryRepository.findByTransferTypeCode(ttCode);
        BookkeepingDTO bkDTO = new BookkeepingDTO();
        List<Cashbk> cashBkList = new ArrayList<>();
        List<Secbk> secBkList = new ArrayList<>();
        bkEntries.ifPresent(bookkeepingEntries -> bookkeepingEntries.forEach(bk -> {
            if (!StringUtils.isBlank(bk.getIssueTypeCode()) && bk.getIssueTypeCode().trim().equalsIgnoreCase(Constants.ISSUE_TYPE_CASH)) {
                Cashbk cashbk = new Cashbk();
                cashbk.setId(bk.getBookkeepingEntryId());
                cashbk.setCloseType(!StringUtils.isBlank(bk.getCloseTypeCode()) ? bk.getCloseTypeCode() : null);
                cashbk.setOttDebitLong(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getOttDleAccountNumber(), bk.getOttDleAccountType(), bk.getOttDleAccountCheckDigit()));
                cashbk.setOttCreditShort(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getOttCseAccountNumber(), bk.getOttCseAccountType(), bk.getOttCseAccountCheckDigit()));
                cashbk.setClosedCreditShort(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getCloseCseAccountNumber(), bk.getCloseCseAccountType(), bk.getCloseCseAccountCheckDigit()));
                cashbk.setClosedDebitLong(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getCloseDleAccountNumber(), bk.getCloseDleAccountType(), bk.getCloseDleAccountCheckDigit()));
                cashBkList.add(cashbk);
            } else if (!StringUtils.isBlank(bk.getIssueTypeCode()) && bk.getIssueTypeCode().trim().equalsIgnoreCase(Constants.ISSUE_TYPE_SECURITY)) {
                Secbk secbk = new Secbk();
                secbk.setId(bk.getBookkeepingEntryId());
                secbk.setCloseType(!StringUtils.isBlank(bk.getCloseTypeCode()) ? bk.getCloseTypeCode() : null);
                secbk.setOttDebitLong(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getOttDleAccountNumber(), bk.getOttDleAccountType(), bk.getOttDleAccountCheckDigit()));
                secbk.setOttCreditShort(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getOttCseAccountNumber(), bk.getOttCseAccountType(), bk.getOttCseAccountCheckDigit()));
                secbk.setClosedCreditShort(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getCloseCseAccountNumber(), bk.getCloseCseAccountType(), bk.getCloseCseAccountCheckDigit()));
                secbk.setClosedDebitLong(TransferTypeUtil.formatAcctNbrTypeDgt(bk.getCloseDleAccountNumber(), bk.getCloseDleAccountType(), bk.getCloseDleAccountCheckDigit()));
                secBkList.add(secbk);
            }
        }));
        bkDTO.setCashbk(cashBkList);
        bkDTO.setSecbk(secBkList);
        return bkDTO;
    }

    public List<BookkeepingEntry> updateBookkeepingEntries(TransferTypeDTO form) {
        List<BookkeepingEntry> bookkeepingEntries = new ArrayList<>();
        bookkeepingEntries.addAll(updateCashBkEntries(form));
        bookkeepingEntries.addAll(updateSecBkEntries(form));
        return bookkeepingEntries;
    }

    private List<BookkeepingEntry> updateCashBkEntries(TransferTypeDTO form) {
        List<BookkeepingEntry> bkEntries = new ArrayList<>();
        if (form.getBookkeeping() != null && form.getBookkeeping().getCashbk() != null && !form.getBookkeeping().getCashbk().isEmpty() && form.getBookkeeping().getCashbk().size() > 0) {
            for (Cashbk cashbk : form.getBookkeeping().getCashbk()) {
                if (cashbk.getId() != null) {
                    Optional<BookkeepingEntry> bkEntry = bookkeepingEntryRepository.findById(cashbk.getId());
                    if (bkEntry.isPresent()) {
                        if (cashbk.getIsDelete()) {
                            bookkeepingEntryRepository.deleteById(cashbk.getId());
                        } else {
                            bkEntry.get().setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                            bkEntry.get().setIssueTypeCode(Constants.ISSUE_TYPE_CASH);
                            bkEntry.get().setCloseTypeCode(cashbk.getCloseType());

                            EntryAcctNbr ottDebitLongAcctNbr = new EntryAcctNbr(cashbk.getOttDebitLong());
                            bkEntry.get().setOttDleAccountNumber(ottDebitLongAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setOttDleAccountType(ottDebitLongAcctNbr.getEntryAcctType());
                            bkEntry.get().setOttDleAccountCheckDigit(ottDebitLongAcctNbr.getEntryAcctCheckDigit());

                            EntryAcctNbr ottCreditShortAcctNbr = new EntryAcctNbr(cashbk.getOttCreditShort());
                            bkEntry.get().setOttCseAccountNumber(ottCreditShortAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setOttCseAccountType(ottCreditShortAcctNbr.getEntryAcctType());
                            bkEntry.get().setOttCseAccountCheckDigit(ottCreditShortAcctNbr.getEntryAcctCheckDigit());

                            EntryAcctNbr closedDebitLongAcctNbr = new EntryAcctNbr(cashbk.getClosedDebitLong());
                            bkEntry.get().setCloseDleAccountNumber(closedDebitLongAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setCloseDleAccountType(closedDebitLongAcctNbr.getEntryAcctType());
                            bkEntry.get().setCloseDleAccountCheckDigit(closedDebitLongAcctNbr.getEntryAcctCheckDigit());

                            EntryAcctNbr closedCreditShortAcctNbr = new EntryAcctNbr(cashbk.getClosedCreditShort());
                            bkEntry.get().setCloseCseAccountNumber(closedCreditShortAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setCloseCseAccountType(closedCreditShortAcctNbr.getEntryAcctType());
                            bkEntry.get().setCloseCseAccountCheckDigit(closedCreditShortAcctNbr.getEntryAcctCheckDigit());

                            bkEntry.get().setLastUpdateName(form.getLastUpdateName());
                            bkEntry.get().setLastUpdateDt(new Date());
                            bkEntries.add(bkEntry.get());
                            //bookkeepingEntryRepository.save(bkEntry.get());
                        }
                    }
                } else {
                    BookkeepingEntry bkEntry = new BookkeepingEntry();
                    bkEntry.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                    bkEntry.setIssueTypeCode(Constants.ISSUE_TYPE_CASH);
                    bkEntry.setCloseTypeCode(cashbk.getCloseType());

                    EntryAcctNbr ottDebitLongAcctNbr = new EntryAcctNbr(cashbk.getOttDebitLong());
                    bkEntry.setOttDleAccountNumber(ottDebitLongAcctNbr.getEntryAcctNbr());
                    bkEntry.setOttDleAccountType(ottDebitLongAcctNbr.getEntryAcctType());
                    bkEntry.setOttDleAccountCheckDigit(ottDebitLongAcctNbr.getEntryAcctCheckDigit());

                    EntryAcctNbr ottCreditShortAcctNbr = new EntryAcctNbr(cashbk.getOttCreditShort());
                    bkEntry.setOttCseAccountNumber(ottCreditShortAcctNbr.getEntryAcctNbr());
                    bkEntry.setOttCseAccountType(ottCreditShortAcctNbr.getEntryAcctType());
                    bkEntry.setOttCseAccountCheckDigit(ottCreditShortAcctNbr.getEntryAcctCheckDigit());

                    EntryAcctNbr closedDebitLongAcctNbr = new EntryAcctNbr(cashbk.getClosedDebitLong());
                    bkEntry.setCloseDleAccountNumber(closedDebitLongAcctNbr.getEntryAcctNbr());
                    bkEntry.setCloseDleAccountType(closedDebitLongAcctNbr.getEntryAcctType());
                    bkEntry.setCloseDleAccountCheckDigit(closedDebitLongAcctNbr.getEntryAcctCheckDigit());

                    EntryAcctNbr closedCreditShortAcctNbr = new EntryAcctNbr(cashbk.getClosedCreditShort());
                    bkEntry.setCloseCseAccountNumber(closedCreditShortAcctNbr.getEntryAcctNbr());
                    bkEntry.setCloseCseAccountType(closedCreditShortAcctNbr.getEntryAcctType());
                    bkEntry.setCloseCseAccountCheckDigit(closedCreditShortAcctNbr.getEntryAcctCheckDigit());

                    bkEntry.setLastUpdateName(form.getLastUpdateName());
                    bkEntry.setLastUpdateDt(new Date());
                    bkEntries.add(bkEntry);
                }
            }
        }
        return bkEntries;
    }

    private List<BookkeepingEntry> updateSecBkEntries(TransferTypeDTO form) {
        List<BookkeepingEntry> bkEntries = new ArrayList<>();
        if (form.getBookkeeping() != null && form.getBookkeeping().getSecbk() != null && !form.getBookkeeping().getSecbk().isEmpty() && form.getBookkeeping().getSecbk().size() > 0) {
            for (Secbk secbk : form.getBookkeeping().getSecbk()) {
                if (secbk.getId() != null) {
                    Optional<BookkeepingEntry> bkEntry = bookkeepingEntryRepository.findById(secbk.getId());
                    if (bkEntry.isPresent()) {
                        if (secbk.getIsDelete()) {
                            bookkeepingEntryRepository.deleteById(secbk.getId());
                        } else {
                            bkEntry.get().setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                            bkEntry.get().setIssueTypeCode(Constants.ISSUE_TYPE_SECURITY);
                            bkEntry.get().setCloseTypeCode(secbk.getCloseType());

                            EntryAcctNbr ottDebitLongAcctNbr = new EntryAcctNbr(secbk.getOttDebitLong());
                            bkEntry.get().setOttDleAccountNumber(ottDebitLongAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setOttDleAccountType(ottDebitLongAcctNbr.getEntryAcctType());
                            bkEntry.get().setOttDleAccountCheckDigit(ottDebitLongAcctNbr.getEntryAcctCheckDigit());

                            EntryAcctNbr ottCreditShortAcctNbr = new EntryAcctNbr(secbk.getOttCreditShort());
                            bkEntry.get().setOttCseAccountNumber(ottCreditShortAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setOttCseAccountType(ottCreditShortAcctNbr.getEntryAcctType());
                            bkEntry.get().setOttCseAccountCheckDigit(ottCreditShortAcctNbr.getEntryAcctCheckDigit());

                            EntryAcctNbr closedDebitLongAcctNbr = new EntryAcctNbr(secbk.getClosedDebitLong());
                            bkEntry.get().setCloseDleAccountNumber(closedDebitLongAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setCloseDleAccountType(closedDebitLongAcctNbr.getEntryAcctType());
                            bkEntry.get().setCloseDleAccountCheckDigit(closedDebitLongAcctNbr.getEntryAcctCheckDigit());

                            EntryAcctNbr closedCreditShortAcctNbr = new EntryAcctNbr(secbk.getClosedCreditShort());
                            bkEntry.get().setCloseCseAccountNumber(closedCreditShortAcctNbr.getEntryAcctNbr());
                            bkEntry.get().setCloseCseAccountType(closedCreditShortAcctNbr.getEntryAcctType());
                            bkEntry.get().setCloseCseAccountCheckDigit(closedCreditShortAcctNbr.getEntryAcctCheckDigit());

                            bkEntry.get().setLastUpdateName(form.getLastUpdateName());
                            bkEntry.get().setLastUpdateDt(new Date());
                            bkEntries.add(bkEntry.get());
                            //bookkeepingEntryRepository.save(bkEntry.get());
                        }
                    }
                } else {
                    BookkeepingEntry bkEntry = new BookkeepingEntry();
                    bkEntry.setTransferTypeCode(TransferTypeUtil.getTransferTypeCodeFromFormData(form));
                    bkEntry.setIssueTypeCode(Constants.ISSUE_TYPE_SECURITY);
                    bkEntry.setCloseTypeCode(secbk.getCloseType());

                    EntryAcctNbr ottDebitLongAcctNbr = new EntryAcctNbr(secbk.getOttDebitLong());
                    bkEntry.setOttDleAccountNumber(ottDebitLongAcctNbr.getEntryAcctNbr());
                    bkEntry.setOttDleAccountType(ottDebitLongAcctNbr.getEntryAcctType());
                    bkEntry.setOttDleAccountCheckDigit(ottDebitLongAcctNbr.getEntryAcctCheckDigit());

                    EntryAcctNbr ottCreditShortAcctNbr = new EntryAcctNbr(secbk.getOttCreditShort());
                    bkEntry.setOttCseAccountNumber(ottCreditShortAcctNbr.getEntryAcctNbr());
                    bkEntry.setOttCseAccountType(ottCreditShortAcctNbr.getEntryAcctType());
                    bkEntry.setOttCseAccountCheckDigit(ottCreditShortAcctNbr.getEntryAcctCheckDigit());

                    EntryAcctNbr closedDebitLongAcctNbr = new EntryAcctNbr(secbk.getClosedDebitLong());
                    bkEntry.setCloseDleAccountNumber(closedDebitLongAcctNbr.getEntryAcctNbr());
                    bkEntry.setCloseDleAccountType(closedDebitLongAcctNbr.getEntryAcctType());
                    bkEntry.setCloseDleAccountCheckDigit(closedDebitLongAcctNbr.getEntryAcctCheckDigit());

                    EntryAcctNbr closedCreditShortAcctNbr = new EntryAcctNbr(secbk.getClosedCreditShort());
                    bkEntry.setCloseCseAccountNumber(closedCreditShortAcctNbr.getEntryAcctNbr());
                    bkEntry.setCloseCseAccountType(closedCreditShortAcctNbr.getEntryAcctType());
                    bkEntry.setCloseCseAccountCheckDigit(closedCreditShortAcctNbr.getEntryAcctCheckDigit());

                    bkEntry.setLastUpdateName(form.getLastUpdateName());
                    bkEntry.setLastUpdateDt(new Date());
                    bkEntries.add(bkEntry);
                    //bookkeepingEntryRepository.save(bkEntry.get());
                }
            }
        }
        return bkEntries;
    }
}