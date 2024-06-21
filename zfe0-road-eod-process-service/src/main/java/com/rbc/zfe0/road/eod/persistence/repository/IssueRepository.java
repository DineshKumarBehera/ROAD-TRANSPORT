package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.dto.IIssueExportSummary;
import com.rbc.zfe0.road.eod.persistence.entity.Issue;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Integer> {

    @Query(value = "select i from Issue i " +
            "where i.issueID in " +
            "(select issue.issueID from Issue issue , TransferItem ti " +
            "where issue.transferItem.transferItemId=ti.transferItemId and ti.statusCode='CLOS')")
    List<Issue> deleteClosedIssues();

    @Query(value = "select i from Issue i where i.entryDate is NULL and i.cseAccountNumber<>'09000097' and i.transferItem.transferItemId in (select ti.transferItemId from TransferItem ti where ti.dispositionCode='UU97')")
    List<Issue> getRecordsUpdateToBox97Query();

    @Query(value="select i from Issue i where i.cseAccountNumber='09000097' and i.transferItem.transferItemId in (select ti.transferItemId from TransferItem ti where ti.dispositionCode='CF00')")
    List<Issue> getRecordsUpdateFromBox97Query();

    List<Issue> findByBlockFlag(Integer blockFlag);

    @Query("SELECT i.issueID AS issueID, " +
            "i.transferItem AS transferItem, " +
            "i.batchCode AS batchCode, " +
            "i.cashEntryFlag AS cashEntryFlag, " +
            "i.insuranceValue AS insuranceValue, " +
            "i.checkNumber AS checkNumber, " +
            "i.checkAmount AS checkAmount, " +
            "i.certificateNumber AS certificateNumber, " +
            "i.certificateDate AS certificateDate, " +
            "i.quantity AS quantity, " +
            "i.dainSecurityNumber AS dainSecurityNumber, " +
            "i.adpSecurityNumber AS adpSecurityNumber, " +
            "i.denomination AS denomination, " +
            "i.entryDate AS entryDate, " +
            "i.entryCode AS entryCode, " +
            "i.dleAccountNumber AS dleAccountNumber, " +
            "i.dleAccountType AS dleAccountType, " +
            "i.dleAccountCheckDigit AS dleAccountCheckDigit, " +
            "i.cseAccountNumber AS cseAccountNumber, " +
            "i.cseAccountType AS cseAccountType, " +
            "i.cseAccountCheckDigit AS cseAccountCheckDigit, " +
            "i.cusip AS cusip, " +
            "i.securityDescription AS securityDescription, " +
            "i.blockFlag AS blockFlag, " +
            "i.lastUpdateName AS lastUpdateName, " +
            "i.lastUpdateDt AS lastUpdateDt, " +
            "i.closeFlag AS closeFlag " +
            "FROM Issue i " +
            "LEFT JOIN i.transferItem ti " +
            "WHERE i.transferItem.statusCode = :statusCode " +
            "ORDER BY i.transferItem.transferItemId")
    List<IIssueExportSummary> findTransferItemsWithIssueByStatusCode(@Param("statusCode") String statusCode);


    @Query("SELECT i.issueID, " +
            "i.transferItem, " +
            "i.batchCode, " +
            "i.cashEntryFlag, " +
            "i.insuranceValue, " +
            "i.checkNumber, " +
            "i.checkAmount, " +
            "i.certificateNumber, " +
            "i.certificateDate, " +
            "i.quantity, " +
            "i.dainSecurityNumber, " +
            "i.adpSecurityNumber, " +
            "i.denomination, " +
            "i.entryDate, " +
            "i.entryCode, " +
            "i.dleAccountNumber, " +
            "i.dleAccountType, " +
            "i.dleAccountCheckDigit, " +
            "i.cseAccountNumber, " +
            "i.cseAccountType, " +
            "i.cseAccountCheckDigit, " +
            "i.cusip, " +
            "i.securityDescription, " +
            "i.blockFlag, " +
            "i.lastUpdateName, " +
            "i.lastUpdateDt, " +
            "i.closeFlag " +
            "FROM Issue i " +
            "LEFT JOIN i.transferItem ti " +
            "WHERE ti.transferItemId = :transferItemId " +
            "ORDER BY ti.transferItemId")
    IIssueExportSummary findIssueByTransferItemId(Integer transferItemId);
}
