package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.persistence.entity.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Integer> {

    @Query(value="select ih from IssueHistory ih where ih.transferItemHistory.transferItemHistoryId=?1")
    List<IssueHistory> deleteIssueHistory(Integer tiHistoryId);

    Optional<IssueHistory> findByIssueId(Integer issueId);
}