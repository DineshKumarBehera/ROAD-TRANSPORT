package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
}
