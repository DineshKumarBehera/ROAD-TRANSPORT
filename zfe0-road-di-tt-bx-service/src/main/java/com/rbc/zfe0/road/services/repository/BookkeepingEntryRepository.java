package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.BookkeepingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookkeepingEntryRepository extends JpaRepository<BookkeepingEntry, Integer> {

    Optional<List<BookkeepingEntry>> findByTransferTypeCode(String ttCode);
}
