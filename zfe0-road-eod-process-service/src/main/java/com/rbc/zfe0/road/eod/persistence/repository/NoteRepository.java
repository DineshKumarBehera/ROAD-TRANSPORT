package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
}
