package com.rbc.zfe0.road.services.repository;


import com.rbc.zfe0.road.services.entity.NoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteHistoryRepository extends JpaRepository<NoteHistory, Integer> {


}