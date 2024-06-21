package com.rbc.zfe0.road.services.transferitem.repository;

import com.rbc.zfe0.road.services.transferitem.dto.ITransferItemExportSummary;
import com.rbc.zfe0.road.services.transferitem.entity.TransferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TransferItemRepository provides the mechanism for storage, retrieval, search,
 * update and delete operation on objects.
 */
@Repository
public interface TransferItemRepository extends JpaRepository<TransferItem, Integer>, JpaSpecificationExecutor<TransferItem> {
    List<TransferItem> findByStatusCode(String statusCode);
    TransferItem findById(int id);
    @Modifying
    @Query(value = "delete from TransferItem ti where ti.transferItemId= :transferItemId")
    void deleteTransferItemByItemId(@Param("transferItemId") Integer transferItemId);

    @Query(value = "SELECT ti.transferItemId AS transferItemId, " +
            "ti.controlId AS controlId, " +
            "ti.adpBranchCode AS adpBranchCode, " +
            "ti.dispositionCode AS dispositionCode, " +
            "ti.dispositionDt AS dispositionDt, " +
            "ti.altBranchCode AS altBranchCode, " +
            "ti.repId AS repId," +
            "ti.receivedDt AS receivedDt," +
            "ti.confirmationReceivedDt AS confirmationReceivedDt," +
            "ti.confirmationSentDt AS confirmationSentDt," +
            "ti.originalCusIp AS originalCusIp, " +
            "ti.statusCode AS statusCode, " +
            "ti.originalQty AS originalQuantity, " +
            "ti.originalSecurityDescr AS originalSecurityDescr, " +
            "ti.closeDt AS closeDt, " +
            "ti.originalCseaAccountNumber AS originalCseaAccountNumber, " +
            "ti.originalCseaAccountType AS originalCseaAccountType, " +
            "ti.originalCseaAccountCheckDigit AS originalCseaAccountCheckDigit, " +
            "ti.transferredDt AS transferredDt, " +
            "ti.originalAdpSecurityNumber AS originalAdpSecurityNumber, " +
            "ti.adpAccountNumber AS adpAccountNumber, " +
            "ti.adpAccountType AS adpAccountType, " +
            "ti.lastUpdateName AS lastUpdateName, " +
            "ti.lastUpdateDt AS lastUpdateDt, " +
            "ti.transferAgent.transferAgentId AS transferAgentId, " +
            "ti.registrationTaxId AS registrationTaxId, " +
            "ti.registrationDescr AS registrationDescr, " +
            "ti.mailReceiptNumber AS mailReceiptNumber, " +
            "ti.accountTaxId AS accountTaxId, " +
            "ti.transferType.transferTypeCode AS transferTypeCode, " +
            "i.issueID AS issueID, " +
            "i.quantity AS quantity, " +
            "i.adpSecurityNumber AS adpSecurityNumber, " +
            "i.entryCode AS entryCode, " +
            "i.cusip AS cusip, " +
            "i.entryDate As entryDate," +
            "i.securityDescription AS securityDescription, " +
            "i.checkAmount AS checkAmount, " +
            "i.cseAccountNumber AS cseAccountNumber, " +
            "i.cseAccountType AS cseAccountType, " +
            "i.cseAccountCheckDigit AS cseAccountCheckDigit, " +
            "i.cashEntryFlag AS cashEntryFlag, " +
            "i.certificateNumber AS certificateNumber, " +
            "i.transferItem.issue AS issue, " +
            "di.deliveryInstructionId AS deliveryInstructionId, " +
            "ti.deliveryInstruction.deliveryInstructionName AS deliveryInstructionName, " +
            "CASE " +
            "    WHEN i.transferItem IS NOT NULL AND (SELECT COUNT(*) FROM Issue WHERE transferItem = ti) > 0 THEN 'NI' " +
            "    ELSE 'OI' " +
            "END AS itemType " +
            "FROM TransferItem ti " +
            "LEFT JOIN ti.issue i " +
            "LEFT JOIN ti.deliveryInstruction di " +
            "WHERE ti.statusCode = :statusCode " +
            "ORDER BY ti.transferItemId")
    List<ITransferItemExportSummary> getTransferItemsByStatusCode(@Param("statusCode") String statusCode);
    @Query(value = "SELECT ti.transferItemId AS transferItemId, " +
            "ti.controlId AS controlId, " +
            "ti.adpBranchCode AS adpBranchCode, " +
            "ti.dispositionCode AS dispositionCode, " +
            "ti.dispositionDt AS dispositionDt, " +
            "ti.altBranchCode AS altBranchCode, " +
            "ti.repId AS repId," +
            "ti.receivedDt AS receivedDt," +
            "ti.confirmationReceivedDt AS confirmationReceivedDt," +
            "ti.confirmationSentDt AS confirmationSentDt," +
            "ti.originalCusIp AS originalCusIp, " +
            "ti.statusCode AS statusCode, " +
            "ti.originalQty AS originalQuantity, " +
            "ti.originalSecurityDescr AS originalSecurityDescr, " +
            "ti.closeDt AS closeDt, " +
            "ti.originalCseaAccountNumber AS originalCseaAccountNumber, " +
            "ti.originalCseaAccountType AS originalCseaAccountType, " +
            "ti.originalCseaAccountCheckDigit AS originalCseaAccountCheckDigit, " +
            "ti.transferredDt AS transferredDt, " +
            "ti.originalAdpSecurityNumber AS originalAdpSecurityNumber, " +
            "ti.adpAccountNumber AS adpAccountNumber, " +
            "ti.adpAccountType AS adpAccountType, " +
            "ti.lastUpdateName AS lastUpdateName, " +
            "ti.lastUpdateDt AS lastUpdateDt, " +
            "ti.transferAgent.transferAgentId AS transferAgentId, " +
            "ti.registrationTaxId AS registrationTaxId, " +
            "ti.registrationDescr AS registrationDescr, " +
            "ti.mailReceiptNumber AS mailReceiptNumber, " +
            "ti.accountTaxId AS accountTaxId, " +
            "ti.transferType.transferTypeCode AS transferTypeCode, " +
            "i.issueID AS issueID, " +
            "i.quantity AS quantity, " +
            "i.adpSecurityNumber AS adpSecurityNumber, " +
            "i.entryCode AS entryCode, " +
            "i.cusip AS cusip, " +
            "i.entryDate As entryDate," +
            "i.securityDescription AS securityDescription, " +
            "i.checkAmount AS checkAmount, " +
            "i.cseAccountNumber AS cseAccountNumber, " +
            "i.cseAccountType AS cseAccountType, " +
            "i.cseAccountCheckDigit AS cseAccountCheckDigit, " +
            "i.cashEntryFlag AS cashEntryFlag, " +
            "i.certificateNumber AS certificateNumber, " +
            "i.transferItem.issue AS issue, " +
            "di.deliveryInstructionId AS deliveryInstructionId, " +
            "ti.deliveryInstruction.deliveryInstructionName AS deliveryInstructionName, " +
            "CASE " +
            "    WHEN i.transferItem IS NOT NULL AND (SELECT COUNT(*) FROM Issue WHERE transferItem = ti) > 0 THEN 'NI' " +
            "    ELSE 'OI' " +
            "END AS itemType " +
            "FROM TransferItem ti " +
            "LEFT JOIN ti.issue i " +
            "LEFT JOIN ti.deliveryInstruction di " +
            "WHERE ti.statusCode = :statusCode " +
            "AND ti.transferItemId IN :transferItemIds " +
            "ORDER BY ti.transferItemId")
    List<ITransferItemExportSummary> getTransferItemsByStatusCodeAndItemId(
            @Param("statusCode") String statusCode,
            @Param("transferItemIds") List<Integer> transferItemIds);
}

