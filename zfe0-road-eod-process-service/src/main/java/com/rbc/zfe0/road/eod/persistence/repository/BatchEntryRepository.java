package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.BatchEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchEntryRepository extends JpaRepository<BatchEntry, Integer> {

    @Query(value = "select be from BatchEntry be where be.transferTypeCode = ?1 AND be.statusCode = ?2 AND be.dummyAccountFlag = ?3 AND be.box97EntryFlag = ?4 AND be.issueTypeCode = ?5")
    List<BatchEntry> getOttBatchEntryCodes(String transferTypeCode, String statusCode, Integer dummyAcct, Integer box97Entry, String issueTypeCode);

    @Query(value = "select be from BatchEntry be where be.transferTypeCode = ?1 AND be.statusCode = ?2 AND be.dummyAccountFlag = ?3 AND be.box97EntryFlag = ?4 AND be.issueTypeCode = ?5 AND be.entryType not in ('CLRJ','CLCF','CLWL','CLES')")
    List<BatchEntry> getNormalCloseBatchEntryCodes(String transferTypeCode, String statusCode, Integer dummyAcct, Integer box97Entry, String issueTypeCode);

    @Query(value = "select be from BatchEntry be where be.transferTypeCode = ?1 AND be.statusCode = ?2 AND be.dummyAccountFlag = ?3 AND be.box97EntryFlag = ?4 AND be.issueTypeCode = ?5")
    List<BatchEntry> getNonNormalCloseBatchEntryCodes(String transferTypeCode, String statusCode, Integer dummyAcct, Integer box97Entry, String issueTypeCode);

    @Query(value = "select be from BatchEntry be where be.transferTypeCode=?1 order by be.memoAccountNumber, be.memoAccountType, be.memoAccountCheckDigit")
    List<BatchEntry> getBoxLocationEntries(String transferTypeCode);
}
