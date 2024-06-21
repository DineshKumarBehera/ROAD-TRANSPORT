package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.NoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NoteHistoryRepository extends JpaRepository<NoteHistory, Integer> {

    @Query(value="select nh from NoteHistory nh where nh.transferItemHistoryId=?1")
    List<NoteHistory> deleteNoteHistory(Integer tiHistoryId);
}