package com.rbc.zfe0.road.services.repository;

import com.rbc.zfe0.road.services.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {

    @Query(value = "select i from Issue i where (i.dleAccountNumber= :acctNo and i.dleAccountType= :acctType and i.dleAccountCheckDigit= :acctChkDgt) or (i.cseAccountNumber= :acctNo and i.cseAccountType= :acctType and i.cseAccountCheckDigit= :acctChkDgt)")
    List<Issue> findByBoxLocationInUse(@Param("acctNo")String acctNo, @Param("acctType") String acctType, @Param("acctChkDgt") String acctChkDgt);
}
