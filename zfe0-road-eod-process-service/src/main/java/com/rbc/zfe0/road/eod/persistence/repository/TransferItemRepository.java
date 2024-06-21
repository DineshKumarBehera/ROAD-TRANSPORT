package com.rbc.zfe0.road.eod.persistence.repository;

import com.rbc.zfe0.road.eod.dto.ITransferItemExportSummary;
import com.rbc.zfe0.road.eod.process.handler.mapper.IEodTransferItem;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.persistence.entity.Issue;
import com.rbc.zfe0.road.eod.persistence.entity.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransferItemRepository extends JpaRepository<TransferItem, Integer> {

    Optional<List<TransferItem>> findByStatusCodeAndCloseDtGreaterThan(String statusCode, Date closeDt);

    Optional<List<TransferItem>> findByCloseDtIsNotNull();

    Optional<List<TransferItem>> findByBlockFlag(Integer blockFlag);

    @Query(value = "Select ti.transferItemId as transferItemId,\n" +
            "            ti.controlId as controlId,\n" +
            "            ti.adpBranchCode as adpBranchCode,\n" +
            "            ti.dispositionCode as dispositionCode,\n" +
            "            ti.dispositionDt as dispositionDt,\n" +
            "            ti.altBranchCode as altBranchCode, \n" +
            "            ti.originalCusIp as OriginalCusIP, \n" +
            "            ti.statusCode as statusCode,\n" +
            "            ti.originalQty as originalQuantity,\n" +
            "            ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "            ti.closeDt as closeDate,\n" +
            "            ti.originalCseAccountNumber as originalCSEAccountNumber,\n" +
            "            ti.originalCseAccountType as originalCSEAccountType,\n" +
            "            ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit,\n" +
            "            ti.transferredDt as transferredDate,\n" +
            "            ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "            ti.adpAccountNumber as adpAccountNumber,\n" +
            "            ti.adpAccountType as adpAccountType,\n" +
            "            ti.lastUpdateName as lastUpdatedName, \n" +
            "            ti.lastUpdateDt as lastUpdatedDate,\n" +
            "            ti.transferAgentId as transferAgentId,\n" +
            "            ti.registrationTaxId as registrationTaxId,\n" +
            "            ti.registrationDescr as registrationDesc,\n" +
            "            ti.mailReceiptNumber as mailReceiptNumber, \n" +
            "            ti.accountTaxId as accountTaxId,\n" +
            "            ti.transferType.transferTypeCode as transferTypeCode,\n" +
            "            i.issueID as issueId,\n" +
            "            i.quantity as quantity, \n" +
            "            i.adpSecurityNumber as adpSecurityNumber,\n" +
            "            i.entryCode as entryCode,\n" +
            "            i.cusip as cusIP,\n" +
            "            i.securityDescription as securityDescr,\n" +
            "            i.checkAmount as checkAmount,\n" +
            "            i.cseAccountNumber as CSEAccountNumber,\n" +
            "            i.cseAccountType as CSEAccountType,\n" +
            "            i.cseAccountCheckDigit as CSEAccountCheckDigit,\n" +
            "            i.cashEntryFlag as cashEntryFlag,\n" +
            "            i.certificateNumber as certificateNumber \n" +
            "            from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.statusCode= :statusCode and ti.transferredDt= :transferredDt")
    Optional<List<IEodTransferItem>> findByStatusCodeAndTransferredDt(@Param("statusCode") String statusCode, @Param("transferredDt") Date transferredDt);
    @Query(value = "Select ti.transferItemId as transferItemId,\n" +
            "            ti.controlId as controlId,\n" +
            "            ti.adpBranchCode as adpBranchCode,\n" +
            "            ti.dispositionCode as dispositionCode,\n" +
            "            ti.dispositionDt as dispositionDt,\n" +
            "            ti.altBranchCode as altBranchCode, \n" +
            "            ti.originalCusIp as OriginalCusIP, \n" +
            "            ti.statusCode as statusCode,\n" +
            "            ti.originalQty as originalQuantity,\n" +
            "            ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "            ti.closeDt as closeDate,\n" +
            "            ti.originalCseAccountNumber as originalCSEAccountNumber,\n" +
            "            ti.originalCseAccountType as originalCSEAccountType,\n" +
            "            ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit,\n" +
            "            ti.transferredDt as transferredDate,\n" +
            "            ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "            ti.adpAccountNumber as adpAccountNumber,\n" +
            "            ti.adpAccountType as adpAccountType,\n" +
            "            ti.lastUpdateName as lastUpdatedName, \n" +
            "            ti.lastUpdateDt as lastUpdatedDate,\n" +
            "            ti.transferAgentId as transferAgentId,\n" +
            "            ti.registrationTaxId as registrationTaxId,\n" +
            "            ti.registrationDescr as registrationDesc,\n" +
            "            ti.mailReceiptNumber as mailReceiptNumber, \n" +
            "            ti.accountTaxId as accountTaxId,\n" +
            "            ti.transferType.transferTypeCode as transferTypeCode,\n" +
            "            i.issueID as issueId,\n" +
            "            i.quantity as quantity, \n" +
            "            i.adpSecurityNumber as adpSecurityNumber,\n" +
            "            i.entryCode as entryCode,\n" +
            "            i.cusip as cusIP,\n" +
            "            i.securityDescription as securityDescr,\n" +
            "            i.checkAmount as checkAmount,\n" +
            "            i.cseAccountNumber as CSEAccountNumber,\n" +
            "            i.cseAccountType as CSEAccountType,\n" +
            "            i.cseAccountCheckDigit as CSEAccountCheckDigit,\n" +
            "            i.cashEntryFlag as cashEntryFlag,\n" +
            "            i.certificateNumber as certificateNumber \n" +
            "            from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.statusCode= :statusCode and ti.transferredDt= :transferredDt")
    List<IEodTransferItem> getEODItemsByStatusCodeAndTransferredDt(@Param("statusCode") String statusCode, @Param("transferredDt") Date transferredDt);

    @Query(value = "Select ti.transferItemId as transferItemId, " +
            "ti.controlId as controlId, " +
            "ti.adpBranchCode as adpBranchCode, " +
            "ti.dispositionCode as dispositionCode, " +
            "ti.dispositionDt as dispositionDt, " +
            "ti.altBranchCode as altBranchCode, " +
            "ti.originalCusIp as OriginalCusIP, " +
            "ti.statusCode as statusCode, " +
            "ti.originalQty as originalQuantity, " +
            "ti.originalSecurityDescr as orgSecurityDescr, " +
            "ti.closeDt as closeDate, " +
            "ti.originalCseAccountNumber as originalCSEAccountNumber, " +
            "ti.originalCseAccountType as originalCSEAccountType, " +
            "ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit, " +
            "ti.transferredDt as transferredDate, " +
            "ti.originalAdpSecurityNumber as originalAdpSecurityNumber, " +
            "ti.adpAccountNumber as adpAccountNumber, " +
            "ti.adpAccountType as adpAccountType, " +
            "ti.lastUpdateName as lastUpdatedName, " +
            "ti.lastUpdateDt as lastUpdatedDate, " +
            "ti.transferAgentId as transferAgentId, " +
            "ti.registrationTaxId as registrationTaxId, " +
            "ti.registrationDescr as registrationDesc, " +
            "ti.mailReceiptNumber as mailReceiptNumber, " +
            "ti.accountTaxId as accountTaxId, " +
            "ti.transferType.transferTypeCode as transferTypeCode, " +
            "i.issueID as issueId, " +
            "i.quantity as quantity, " +
            "i.adpSecurityNumber as adpSecurityNumber, " +
            "i.entryCode as entryCode, " +
            "i.cusip as cusIP, " +
            "i.securityDescription as securityDescr, " +
            "i.checkAmount as checkAmount, " +
            "i.cseAccountNumber as CSEAccountNumber, " +
            "i.cseAccountType as CSEAccountType, " +
            "i.cseAccountCheckDigit as CSEAccountCheckDigit, " +
            "i.cashEntryFlag as cashEntryFlag, " +
            "i.certificateNumber as certificateNumber " +
            "from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.statusCode= :statusCode")
    Optional<List<IEodTransferItem>> getEODItemsByStatusCode(@Param("statusCode") String statusCode);

    @Query(value = "Select ti.transferItemId as transferItemId, " +
            "ti.controlId as controlId, " +
            "ti.adpBranchCode as adpBranchCode, " +
            "ti.dispositionCode as dispositionCode, " +
            "ti.dispositionDt as dispositionDt, " +
            "ti.altBranchCode as altBranchCode, " +
            "ti.originalCusIp as OriginalCusIP, " +
            "ti.statusCode as statusCode, " +
            "ti.originalQty as originalQuantity, " +
            "ti.originalSecurityDescr as orgSecurityDescr, " +
            "ti.closeDt as closeDate, " +
            "ti.originalCseAccountNumber as originalCSEAccountNumber, " +
            "ti.originalCseAccountType as originalCSEAccountType, " +
            "ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit, " +
            "ti.transferredDt as transferredDate, " +
            "ti.originalAdpSecurityNumber as originalAdpSecurityNumber, " +
            "ti.adpAccountNumber as adpAccountNumber, " +
            "ti.adpAccountType as adpAccountType, " +
            "ti.lastUpdateName as lastUpdatedName, " +
            "ti.lastUpdateDt as lastUpdatedDate, " +
            "ti.transferAgentId as transferAgentId, " +
            "ti.registrationTaxId as registrationTaxId, " +
            "ti.registrationDescr as registrationDesc, " +
            "ti.mailReceiptNumber as mailReceiptNumber, " +
            "ti.accountTaxId as accountTaxId, " +
            "ti.transferType.transferTypeCode as transferTypeCode, " +
            "i.issueID as issueId, " +
            "i.quantity as quantity, " +
            "i.adpSecurityNumber as adpSecurityNumber, " +
            "i.entryCode as entryCode, " +
            "i.cusip as cusIP, " +
            "i.securityDescription as securityDescr, " +
            "i.checkAmount as checkAmount, " +
            "i.cseAccountNumber as CSEAccountNumber, " +
            "i.cseAccountType as CSEAccountType, " +
            "i.cseAccountCheckDigit as CSEAccountCheckDigit, " +
            "i.cashEntryFlag as cashEntryFlag, " +
            "i.certificateNumber as certificateNumber " +
            "from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.statusCode= :statusCode and ti.closeDt= :closeDt")
    Optional<List<IEodTransferItem>> getEODItemsByStatusCodeAndCloseDt(@Param("statusCode") String statusCode, @Param("closeDt") Date closeDt);

    @Query(value = "Select ti.transferItemId as transferItemId,\n" +
            "            ti.controlId as controlId,\n" +
            "            ti.adpBranchCode as adpBranchCode,\n" +
            "            ti.dispositionCode as dispositionCode,\n" +
            "            ti.dispositionDt as dispositionDt,\n" +
            "            ti.altBranchCode as altBranchCode, \n" +
            "            ti.originalCusIp as OriginalCusIP, \n" +
            "            ti.statusCode as statusCode,\n" +
            "            ti.originalQty as originalQuantity,\n" +
            "            ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "            ti.closeDt as closeDate,\n" +
            "            ti.originalCseAccountNumber as originalCSEAccountNumber,\n" +
            "            ti.originalCseAccountType as originalCSEAccountType,\n" +
            "            ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit,\n" +
            "            ti.transferredDt as transferredDate,\n" +
            "            ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "            ti.adpAccountNumber as adpAccountNumber,\n" +
            "            ti.adpAccountType as adpAccountType,\n" +
            "            ti.lastUpdateName as lastUpdatedName, \n" +
            "            ti.lastUpdateDt as lastUpdatedDate,\n" +
            "            ti.transferAgentId as transferAgentId,\n" +
            "            ti.registrationTaxId as registrationTaxId,\n" +
            "            ti.registrationDescr as registrationDesc,\n" +
            "            ti.mailReceiptNumber as mailReceiptNumber, \n" +
            "            ti.accountTaxId as accountTaxId,\n" +
            "            ti.transferType.transferTypeCode as transferTypeCode,\n" +
            "            i.issueID as issueId,\n" +
            "            i.quantity as quantity, \n" +
            "            i.adpSecurityNumber as adpSecurityNumber,\n" +
            "            i.entryCode as entryCode,\n" +
            "            i.cusip as cusIP,\n" +
            "            i.securityDescription as securityDescr,\n" +
            "            i.checkAmount as checkAmount,\n" +
            "            i.cseAccountNumber as CSEAccountNumber,\n" +
            "            i.cseAccountType as CSEAccountType,\n" +
            "            i.cseAccountCheckDigit as CSEAccountCheckDigit,\n" +
            "            i.cashEntryFlag as cashEntryFlag,\n" +
            "            i.certificateNumber as certificateNumber \n" +
            "            from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.statusCode= :statusCode and i.entryDate= :entryDate")
    Optional<List<IEodTransferItem>> getEodItemsByStatusCodeAndEntryDt(@Param("statusCode") String statusCode, @Param("entryDate") Date entryDate);
    @Query(value = "Select ti.transferItemId as transferItemId,\n" +
            "            ti.controlId as controlId,\n" +
            "            ti.adpBranchCode as adpBranchCode,\n" +
            "            ti.dispositionCode as dispositionCode,\n" +
            "            ti.dispositionDt as dispositionDt,\n" +
            "            ti.altBranchCode as altBranchCode, \n" +
            "            ti.originalCusIp as OriginalCusIP, \n" +
            "            ti.statusCode as statusCode,\n" +
            "            ti.originalQty as originalQuantity,\n" +
            "            ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "            ti.closeDt as closeDate,\n" +
            "            ti.originalCseAccountNumber as originalCSEAccountNumber,\n" +
            "            ti.originalCseAccountType as originalCSEAccountType,\n" +
            "            ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit,\n" +
            "            ti.transferredDt as transferredDate,\n" +
            "            ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "            ti.adpAccountNumber as adpAccountNumber,\n" +
            "            ti.adpAccountType as adpAccountType,\n" +
            "            ti.lastUpdateName as lastUpdatedName, \n" +
            "            ti.lastUpdateDt as lastUpdatedDate,\n" +
            "            ti.transferAgentId as transferAgentId,\n" +
            "            ti.registrationTaxId as registrationTaxId,\n" +
            "            ti.registrationDescr as registrationDesc,\n" +
            "            ti.mailReceiptNumber as mailReceiptNumber, \n" +
            "            ti.accountTaxId as accountTaxId,\n" +
            "            ti.transferType.transferTypeCode as transferTypeCode,\n" +
            "            i.issueID as issueId,\n" +
            "            i.quantity as quantity, \n" +
            "            i.adpSecurityNumber as adpSecurityNumber,\n" +
            "            i.entryCode as entryCode,\n" +
            "            i.cusip as cusIP,\n" +
            "            i.securityDescription as securityDescr,\n" +
            "            i.checkAmount as checkAmount,\n" +
            "            i.cseAccountNumber as CSEAccountNumber,\n" +
            "            i.cseAccountType as CSEAccountType,\n" +
            "            i.cseAccountCheckDigit as CSEAccountCheckDigit,\n" +
            "            i.cashEntryFlag as cashEntryFlag,\n" +
            "            i.certificateNumber as certificateNumber \n" +
            "            from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.closeDt= :closeDt and i.entryDate= :entryDate")
    Optional<List<IEodTransferItem>> getEodItemsByCloseDtAndEntryDt(@Param("closeDt") Date closeDt, @Param("entryDate") Date entryDate);

    @Query(value = "Select ti.transferItemId as transferItemId,\n" +
            "            ti.controlId as controlId,\n" +
            "            ti.adpBranchCode as adpBranchCode,\n" +
            "            ti.dispositionCode as dispositionCode,\n" +
            "            ti.dispositionDt as dispositionDt,\n" +
            "            ti.altBranchCode as altBranchCode, \n" +
            "            ti.originalCusIp as OriginalCusIP, \n" +
            "            ti.statusCode as statusCode,\n" +
            "            ti.originalQty as originalQuantity,\n" +
            "            ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "            ti.closeDt as closeDate,\n" +
            "            ti.originalCseAccountNumber as originalCSEAccountNumber,\n" +
            "            ti.originalCseAccountType as originalCSEAccountType,\n" +
            "            ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit,\n" +
            "            ti.transferredDt as transferredDate,\n" +
            "            ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "            ti.adpAccountNumber as adpAccountNumber,\n" +
            "            ti.adpAccountType as adpAccountType,\n" +
            "            ti.lastUpdateName as lastUpdatedName, \n" +
            "            ti.lastUpdateDt as lastUpdatedDate,\n" +
            "            ti.transferAgentId as transferAgentId,\n" +
            "            ti.registrationTaxId as registrationTaxId,\n" +
            "            ti.registrationDescr as registrationDesc,\n" +
            "            ti.mailReceiptNumber as mailReceiptNumber, \n" +
            "            ti.accountTaxId as accountTaxId,\n" +
            "            ti.transferType.transferTypeCode as transferTypeCode,\n" +
            "            i.issueID as issueId,\n" +
            "            i.quantity as quantity, \n" +
            "            i.adpSecurityNumber as adpSecurityNumber,\n" +
            "            i.entryCode as entryCode,\n" +
            "            i.cusip as cusIP,\n" +
            "            i.securityDescription as securityDescr,\n" +
            "            i.checkAmount as checkAmount,\n" +
            "            i.cseAccountNumber as CSEAccountNumber,\n" +
            "            i.cseAccountType as CSEAccountType,\n" +
            "            i.cseAccountCheckDigit as CSEAccountCheckDigit,\n" +
            "            i.cashEntryFlag as cashEntryFlag,\n" +
            "            i.certificateNumber as certificateNumber \n" +
            "            from com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti left outer join ti.issue i where ti.blockFlag=0 and ti.transferType.autoRoadBlockFlag<>1 and ti.transferItemId= :transferItemId")
    Optional<List<IEodTransferItem>> getEodItemById(@Param("transferItemId") Integer transferItemId);
    @Query(value = "select\n" +
            "    ti.transferItemId as transferItemId,\n" +
            "    ti.dispositionCode as dispositionCode\n" +
            "from\n" +
            "    com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti\n" +
            "where\n" +
            "    ti.statusCode = 'OTT'\n" +
            "    and ti.dispositionDt >= ?1\n" +
            "    and ti.dispositionCode = 'UU97'\n" +
            "    and ti.transferType.transferTypeCode not in (\n" +
            "        select\n" +
            "            tt.transferTypeCode\n" +
            "        from\n" +
            "            TransferType tt\n" +
            "        where\n" +
            "            tt.autoRoadBlockFlag = 1\n" +
            "    )")
    List<IEodTransferItem> getPreUU97OTTRecords(Date dispositionDt);

    @Query(value = "select\n" +
            "    ti.transferItemId as transferItemId,\n" +
            "    ti.dispositionCode as dispositionCode,\n" +
            "    ti.dispositionDt as dispositionDt\n" +
            "from\n" +
            "    com.rbc.zfe0.road.eod.persistence.entity.TransferItem ti\n" +
            "where\n" +
            "    ti.statusCode = 'OTT'\n" +
            "    and ti.dispositionCode <> 'UU97'\n" +
            "    and ti.lastUpdateDt > ?1\n" +
            "    and ti.transferType.transferTypeCode not in (\n" +
            "        select\n" +
            "            tt.transferTypeCode\n" +
            "        from\n" +
            "            TransferType tt\n" +
            "        where\n" +
            "            tt.autoRoadBlockFlag = 1\n" +
            "    )")
    List<IEodTransferItem> getNon97OTTRecords(Date dispositionDt);

    @Query(value = "select\n" +
            "    ti.transferItemId as transferItemId,\n" +
            "    i.cseAccountNumber as CSEAccountNumber,\n" +
            "    i.cseAccountType as CSEAccountType,\n" +
            "    i.cseAccountCheckDigit as CSEAccountCheckDigit,\n" +
            "    ti.adpAccountNumber as adpAccountNumber,\n" +
            "    ti.adpAccountType as adpAccountType,\n" +
            "    ti.adpAccountCheckDigit as adpAccountCheckDigit,\n" +
            "    ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "    ti.originalCusIp as OriginalCusIP,\n" +
            "    ti.originalQty as originalQuantity,\n" +
            "    ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "    ti.transferredDt as transferredDate,\n" +
            "    ti.confirmationReceivedDt as confirmationReceivedDate,\n" +
            "    ti.lastUpdateDt as lastUpdatedDate,\n" +
            "    ti.lastUpdateName as lastUpdatedName,\n" +
            "    i.quantity as quantity,\n" +
            "    i.adpSecurityNumber as adpSecurityNumber,\n" +
            "    i.cusip as cusIP,\n" +
            "    i.securityDescription as securityDescr,\n" +
            "    i.cashEntryFlag as cashEntryFlag\n" +
            "from\n" +
            "    Issue i,\n" +
            "    TransferItem ti\n" +
            "    left outer join TransferType Y on ti.transferType.transferTypeCode = Y.transferTypeCode\n" +
            "where\n" +
            "    i.closeFlag = 0\n" +
            "    AND Y.autoRoadBlockFlag < 1\n" +
            "    AND ti.statusCode = 'OTT'\n" +
            "    AND i.transferItem.transferItemId = ti.transferItemId\n" +
            "    AND i.cseAccountNumber in (\n" +
            "        '09700011',\n" +
            "        '09700012',\n" +
            "        '09700027',\n" +
            "        '09700028',\n" +
            "        '09000097'\n" +
            "    )\n" +
            "order by\n" +
            "    i.cseAccountNumber,\n" +
            "    i.cseAccountType,\n" +
            "    i.cseAccountCheckDigit,\n" +
            "    ti.adpAccountNumber,\n" +
            "    ti.adpAccountType,\n" +
            "    ti.adpAccountCheckDigit,\n" +
            "    ti.originalAdpSecurityNumber,\n" +
            "    ti.originalCusIp")
    List<IEodTransferItem> getICRReportRecs();
    @Query(value = "select\n" +
            "    ti.transferItemId as transferItemId,\n" +
            "    ti.originalCseAccountNumber as originalCSEAccountNumber,\n" +
            "    ti.originalCseAccountType as originalCSEAccountType,\n" +
            "    ti.originalCseAccountCheckDigit as originalCSEAccountCheckDigit,\n" +
            "    ti.adpAccountNumber as adpAccountNumber,\n" +
            "    ti.adpAccountType as adpAccountType,\n" +
            "    ti.adpAccountCheckDigit as adpAccountCheckDigit,\n" +
            "    ti.originalAdpSecurityNumber as originalAdpSecurityNumber,\n" +
            "    ti.originalCusIp as OriginalCusIP,\n" +
            "    ti.originalQty as originalQuantity,\n" +
            "    ti.originalSecurityDescr as orgSecurityDescr,\n" +
            "    ti.transferredDt as transferredDate,\n" +
            "    ti.confirmationReceivedDt as confirmationReceivedDate,\n" +
            "    ti.lastUpdateDt as lastUpdatedDate,\n" +
            "    ti.lastUpdateName as lastUpdatedName\n" +
            "from\n" +
            "    TransferItem ti\n" +
            "    left outer join TransferType Y on ti.transferType.transferTypeCode = Y.transferTypeCode\n" +
            "where\n" +
            "    (\n" +
            "        (Y.autoRoadBlockFlag IS NULL)\n" +
            "        OR (Y.autoRoadBlockFlag < 1)\n" +
            "    )\n" +
            "    AND ti.statusCode = 'Pend'\n" +
            "    AND (\n" +
            "        (\n" +
            "            ti.originalCseAccountNumber = '00000008'\n" +
            "            and ti.originalCseAccountType = '2'\n" +
            "            and ti.originalCseAccountCheckDigit = '8'\n" +
            "        )\n" +
            "        OR (\n" +
            "            ti.originalCseAccountNumber = '09700023'\n" +
            "            and ti.originalCseAccountType = '1'\n" +
            "            and ti.originalCseAccountCheckDigit = '7'\n" +
            "        )\n" +
            "    )\n" +
            "order by\n" +
            "    ti.originalCseAccountNumber,\n" +
            "    ti.originalCseAccountType,\n" +
            "    ti.originalCseAccountCheckDigit,\n" +
            "    ti.adpAccountNumber,\n" +
            "    ti.adpAccountType,\n" +
            "    ti.adpAccountCheckDigit,\n" +
            "    ti.originalAdpSecurityNumber,\n" +
            "    ti.originalCusIp")
    List<IEodTransferItem> getICRPendingReportRecs();

    @Query(value = "select ti from TransferItem ti where ti.confirmationReceivedDt is not null and ti.confirmationReceivedDt<=?1 and ti.confirmationReceivedDt>=?2 and (ti.dispositionCode is null or ti.dispositionCode<>?3) and (ti.statusCode<>'CLOS' and ti.statusCode<>'Pend')")
    List<TransferItem> getDispositionCFQuery(Date minConfRecDt, Date maxConfRecDt, String dispositionCode);

    @Query(value = "select ti from TransferItem ti where ti.confirmationReceivedDt is not null and ti.confirmationReceivedDt>=?1 and (ti.dispositionCode is null or ti.dispositionCode<>?2) and (ti.statusCode<>'CLOS' and ti.statusCode<>'Pend')")
    List<TransferItem> getDispositionCF00Query(Date confRecDt, String dispositionCode);

    @Query(value = "select ti from TransferItem ti where ti.confirmationReceivedDt is not null and ti.transferredDt<=?1 and ti.transferredDt>=?2")
    List<TransferItem> getDispositionTXQuery(Date minTransferredDt, Date maxTransferredDt);

    @Query(value = "select ti from TransferItem ti where ti.confirmationReceivedDt is not null and ti.transferredDt>=?1")
    List<TransferItem> getDispositionTX00Query(Date transferredDt);

    @Query(value = "select ti from TransferItem ti where ti.confirmationReceivedDt is not null and ti.transferredDt<=?1 and ti.confirmationReceivedDt<=?2")
    List<TransferItem> getDispositionUU97Query(Date transferredDt, Date confRecDt);

    @Query(value = "select ti from TransferItem ti where ti.statusCode='CLOS'")
    List<TransferItem> deleteClosedTransferItem();

    /*Query to fetch the DDR Records */

    @Query("SELECT ti FROM TransferItem ti JOIN ti.transferType tt WHERE tt.transferTypeCode = :transferTypeCode")
    List<TransferItem> findByTransferTypeCode(@Param("transferTypeCode") String transferTypeCode);


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
            "ti.originalCseAccountNumber AS originalCseAccountNumber, " +
            "ti.originalCseAccountType AS originalCseAccountType, " +
            "ti.originalCseAccountCheckDigit AS originalCseAccountCheckDigit, " +
            "ti.transferredDt AS transferredDt, " +
            "ti.originalAdpSecurityNumber AS originalAdpSecurityNumber, " +
            "ti.adpAccountNumber AS adpAccountNumber, " +
            "ti.adpAccountType AS adpAccountType, " +
            "ti.lastUpdateName AS lastUpdateName, " +
            "ti.lastUpdateDt AS lastUpdateDt, " +
            "ti.transferAgentId AS transferAgentId, " +
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

}