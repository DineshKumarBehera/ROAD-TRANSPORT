package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.BatchEntry;
import com.rbc.zfe0.road.services.entity.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchEntryRepository extends JpaRepository<BatchEntry, Integer> {

    Optional<List<BatchEntry>> findByTransferTypeCode(String typeCode);

    Optional<List<BatchEntry>> findByTransferTypeCodeAndStatusCodeAndDummyAccountFlagAndBox97EntryFlag(String tt, String sts, Integer dummy, Integer box97);

    Optional<List<BatchEntry>> findByTransferTypeCodeAndEntryTypeAndStatusCodeAndDummyAccountFlagAndBox97EntryFlag(String tt, String et, String sts, Integer dummy, Integer box97);

    Optional<List<BatchEntry>> findByEntryCodeAndTransferTypeCodeNotIn(String entryCode, List<String> ttCode);
}
