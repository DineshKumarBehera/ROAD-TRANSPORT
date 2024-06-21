package com.rbc.zfe0.road.eod.process.handler;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.persistence.entity.Issue;
import com.rbc.zfe0.road.eod.persistence.repository.IssueRepository;
import com.rbc.zfe0.road.eod.process.handler.mapper.IEodTransferItem;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.helper.MFServiceHelper;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItemAudit;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemAuditRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class EodTransit {

    @Autowired
    TransferItemRepository transferItemRepository;

    @Autowired
    TransferItemAuditRepository transferItemAuditRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    MFServiceHelper mfServiceHelper;

    //get Worthless/Escheated/Rejected closed items -not received one
    public List getCewCloseList(Date lastEodDate) {
        log.info("Get Worthless, Escheated, Rejected closed items.");
        List items;
        Optional<List<IEodTransferItem>> list = transferItemRepository.getEodItemsByCloseDtAndEntryDt(lastEodDate, null);
        items = convertToEodTransferItemDTO(list);
        return items;
    }

    //get OTT records with transferred date as last eod date
    public List getOTTRecords(Date lastEodDate) {
        log.info("Get OTT Records with transferred date as last eod date");
        List items;
        Optional<List<IEodTransferItem>> list = transferItemRepository.findByStatusCodeAndTransferredDt(Constants.STATUS_OUT_TO_TRANSFER, lastEodDate);
        items = convertToEodTransferItemDTO(list);
        return items;
    }

    //get CLOS records with entry date as last eod date
    public List getClosedRecords(Date lastEodDate) {
        log.info("Get Closed Records as last eod date.");
        List items = null;
        Optional<List<IEodTransferItem>> list = transferItemRepository.getEodItemsByStatusCodeAndEntryDt(Constants.STATUS_CLOSED, lastEodDate);
        items = convertToEodTransferItemDTO(list);
        return items;
    }

    public List getPartialClosedItems(Date lastEodDate) {
        log.info("Get partially closed items");
        List items = null;
        Optional<List<IEodTransferItem>> list = transferItemRepository.getEodItemsByStatusCodeAndEntryDt(Constants.STATUS_OUT_TO_TRANSFER, lastEodDate);
        items = convertToEodTransferItemDTO(list);
        log.info("getPartialClosedItems  List count " + list.get().size());
        return items;
    }

    /**
     * This method skips issues which are closed as Rejected on the same date(since last EOD run) they transferred to OTT.
     *
     * @param closeList
     * @return List - list of transfer items after skipping Rejected items.
     */
    public List checkCloseForRejected(List closeList) {
        log.info("Skip rejected transfer items from list of transfer item.");
        List itemList = new ArrayList();
        Iterator closeItr = closeList.iterator();
        EodTransferItem eodItem = null;
        while (closeItr.hasNext()) {
            eodItem = (EodTransferItem) closeItr.next();
            //partial close is Normal close.
            if (eodItem.getDispositionCode().equalsIgnoreCase(Constants.CLOSE_TYPE_REJECTED) &&
                    (Utility.checkDateEqual(eodItem.getCloseDate(), eodItem.getTransferredDate()))) {
                log.debug("Found OTT got Rejected on same date for - " + eodItem.getTransferItemId());
            } else {
                itemList.add(eodItem);
            }
        }
        return itemList;
    }
    /**
     * In case of partial close items, update database if account is freezed.
     * The UI will show that item with green mark to show that item is closed even though
     * EOD will not generated Batch and Entry codes for these entries.
     * <p>
     * This method also calls mainframe service to check for account freeze and also skips
     * items which got OTT status and Rejected on same date or after last EOD run.
     */
    public List changeStatusForPartiallyClosedItems(List partialClosedList) throws ServiceLinkException {
        log.info("Change status for partially closed items");
        List itemList = new ArrayList();
        List frozenAccounts = null;
        Iterator frItr = null;
        EodTransferItem eodItem = null;
        frozenAccounts = getFreezedTransferItems(partialClosedList);
        //update partial close in the database as closFlag =2
        if (!Utility.isEmpty(frozenAccounts)) {
            frItr = frozenAccounts.iterator();
            while (frItr.hasNext()) {
                eodItem = (EodTransferItem) frItr.next();
                updatePartialClosedFrozenAccounts(eodItem);
            }
            partialClosedList.removeAll(frozenAccounts);
        }
        Iterator closeItr = partialClosedList.iterator();
        while (closeItr.hasNext()) {
            eodItem = (EodTransferItem) closeItr.next();
            //partial close is Normal close.
            eodItem.setStatusCode(Constants.STATUS_CLOSED);
            eodItem.setDispositionCode(Constants.CLOSE_TYPE_NORMAL);
            itemList.add(eodItem);
        }
        return itemList;
    }

    //TODO: "UPDATE t_Issue SET  closeFlag= 2, LastUpdateDt = ?, LastUpdateName = ? WHERE Issue_ID = ?"
    public void updatePartialClosedFrozenAccounts(EodTransferItem eodItem) throws ServiceLinkException {
        log.info("update partially closed frozen account");
        Optional<Issue> issue = issueRepository.findById(eodItem.getIssueId());
        if(issue.isPresent()) {
            issue.get().setCloseFlag(2);
            issue.get().setLastUpdateDt(new Date());
            issue.get().setLastUpdateName(Constants.APPNAME +"_EOD");
            issueRepository.save(issue.get());
        }
    }

    //get all the freezed accounts
    public List getFreezedTransferItems(List transferItems) throws ServiceLinkException {
        log.info("Get freezed transfer items");
        EodTransferItem item = null;
        Iterator itmItr = transferItems.iterator();
        List freezAccounts = new ArrayList();
        while (itmItr.hasNext()) {
            item = (EodTransferItem) itmItr.next();
            log.debug("Check FROZEN account, TIID: "
                            + item.getTransferItemId() + ", account " + item.getAdpBranchCode().trim(),
                    item.getAdpAccountNumber().trim());
            if (isAccountFreezed(item)) {
                freezAccounts.add(item);
                log.debug("** Found FROZEN account  - " + item.getTransferItemId());
            }
        }
        return freezAccounts;
    }
    //call Stored Proc - RDP0200 to check account can be a part of intact file
    private boolean isAccountFreezed(EodTransferItem item)
            throws ServiceLinkException {
        log.info("call Stored Proc - RDP0200 to check account can be a part of intact file");
        return mfServiceHelper.isAccountFrozen(item.getAdpBranchCode().trim(),
                item.getAdpAccountNumber().trim());
    }

    public List convertToEodTransferItemDTO(Optional<List<IEodTransferItem>> list) {
        log.info("Convert to Eod Transfer Item");
        List<EodTransferItem> eodTransferItems = new ArrayList<>();
        if (list.isPresent()) {
            for (IEodTransferItem ti : list.get()) {
                EodTransferItem item = new EodTransferItem();
                item.setAdpAccountNumber(ti.getAdpAccountNumber() == null ? null : ti.getAdpAccountNumber().trim());
                item.setAdpAccountType(ti.getAdpAccountType() == null ? null : ti.getAdpAccountType().trim());
                item.setAdpAccountCheckDigit(ti.getAdpAccountCheckDigit() == null ? null : ti.getAdpAccountCheckDigit().trim());
                item.setAdpBranchCode(ti.getAdpBranchCode() == null ? null : ti.getAdpBranchCode());

                item.setTransferItemId(ti.getTransferItemId());
                item.setControlId(ti.getControlId() == null ? null : ti.getControlId().trim());
                item.setCloseDate(ti.getCloseDate());
                item.setOriginalQuantity(ti.getOriginalQuantity() == null ? BigDecimal.valueOf(0L) : ti.getOriginalQuantity());
                item.setStatusCode(ti.getStatusCode() == null ? null : ti.getStatusCode().trim());
                item.setDispositionCode(ti.getDispositionCode() == null ? "" : ti.getDispositionCode().trim());
                item.setDispositionDt(ti.getDispositionDt());
                item.setLastUpdatedDate(ti.getLastUpdatedDate());
                item.setLastUpdatedName(ti.getLastUpdatedName() == null ? null : ti.getLastUpdatedName().trim());

                item.setConfirmationReceivedDate(ti.getConfirmationReceivedDate());
                item.setTransferredDate(ti.getTransferredDate());
                item.setOriginalCSEAccountNumber(ti.getOriginalCSEAccountNumber() == null ? "" : ti.getOriginalCSEAccountNumber().trim());
                item.setOriginalCSEAccountType(ti.getOriginalCSEAccountType() == null ? "" : ti.getOriginalCSEAccountType().trim());
                item.setOriginalCSEAccountCheckDigit(ti.getOriginalCSEAccountCheckDigit() == null ? "" : ti.getOriginalCSEAccountCheckDigit().trim());
                item.setTransferTypeCode(ti.getTransferTypeCode() == null ? null : ti.getTransferTypeCode().trim());
                item.setOriginalAdpSecurityNumber(ti.getOriginalAdpSecurityNumber() == null ? null : ti.getOriginalAdpSecurityNumber().trim());
                item.setOriginalCusIP(ti.getOriginalCusIP() == null ? null : ti.getOriginalCusIP().trim());
                item.setOrgSecurityDescr(ti.getOrgSecurityDescr() == null ? "" : ti.getOrgSecurityDescr().trim());
                item.setTransferAgentId(BigDecimal.valueOf(ti.getTransferAgentId()));
                item.setRegistrationTaxId(ti.getRegistrationTaxId() == null ? "" : ti.getRegistrationTaxId().trim());
                item.setRegistrationDesc(ti.getRegistrationDesc() == null ? "" : ti.getRegistrationDesc().trim());
                item.setMailReceiptNumber(ti.getMailReceiptNumber() == null ? "" : ti.getMailReceiptNumber());
                item.setAccountTaxId(ti.getAccountTaxId() == null ? "" : ti.getAccountTaxId().trim());
                item.setCSEAccountNumber(ti.getCSEAccountNumber() == null ? null : ti.getCSEAccountNumber().trim());
                item.setCSEAccountType(ti.getCSEAccountType() == null ? "" : ti.getCSEAccountType().trim());
                item.setCSEAccountCheckDigit(ti.getCSEAccountCheckDigit() == null ? "" : ti.getCSEAccountCheckDigit().trim());
                item.setIssueId(ti.getIssueId());
                item.setQuantity(ti.getQuantity() == null ? BigDecimal.valueOf(0L) : ti.getQuantity());
                item.setCusIP(ti.getCusIP() == null ? "" : ti.getCusIP().trim());
                item.setAdpSecurityNumber(ti.getAdpSecurityNumber() == null ? null : ti.getAdpSecurityNumber().trim());
                item.setCashEntryFlag(ti.getCashEntryFlag());
                item.setSecurityDescr(ti.getSecurityDescr() == null ? "" : ti.getSecurityDescr().trim());
                item.setCertificateNumber(ti.getCertificateNumber() == null ? "" : ti.getCertificateNumber().trim());
                item.setBatchCode(ti.getBatchCode() == null ? "" : ti.getBatchCode().trim());
                item.setEntryCode(ti.getEntryCode() == null ? "" : ti.getEntryCode().trim());
                item.setCheckAmount(String.valueOf(ti.getCheckAmount() == null ? new BigDecimal("0.00") : ti.getCheckAmount()));
                eodTransferItems.add(item);
            }
        }
        return eodTransferItems;

    }
    /**
     * This method gets all transfer items having dispositionCode as UU97 and
     * disposition date as today's date.
     *
     * @param lastEodDate
     * @return List - containing transfer items for UU97
     * @throws ServiceLinkException
     */
    public List getOttUU97List(Date lastEodDate) throws ServiceLinkException {
        log.info("Get all transfer items having dispositionCode as UU97 and disposition date as today's date");
        EodTransferItem item = null;
        List eodList = new ArrayList();
        List ottUU97List = new ArrayList();

        Calendar now = Calendar.getInstance();
        Calendar lastEod = Calendar.getInstance();
        lastEod.setTime(lastEodDate);

        Date pre97Date = lastEodDate;
        if (((now.get(Calendar.MONTH) != lastEod.get(Calendar.MONTH))
                || (now.get(Calendar.DAY_OF_MONTH) != lastEod.get(Calendar.DAY_OF_MONTH))
                || (now.get(Calendar.YEAR) != lastEod.get(Calendar.YEAR)))) {
            pre97Date = Utility.addToDate(lastEodDate, 1);
            log.info("Last EOD was not run today, so retrieve UU97 records for last EOD run + 1 day "
                    + pre97Date);
        } else {
            log.info("Last EOD was run today, so include UU97 records for today");
        }
        //get all the records changed to UU97 for current date.
        ottUU97List.addAll(getPreUU97OTTRecords(pre97Date));

        try {
            List allNon97OttEntries = getNon97OTTRecords(lastEodDate);
            Iterator itr = allNon97OttEntries.iterator();
            List newList = new ArrayList();
            while (itr.hasNext()) {
                EodTransferItem item2 = (EodTransferItem) itr.next();
                log.debug("Checking item " + item2.getTransferItemId()
                        + " for previous disposition code of UU97.");
                boolean prevDispCodeOf97 = true;
                boolean foundOneAuditIssue = false;
                try {
                    List tiIds = new ArrayList();
                    //--tiIds.add(""+item2.getTransferItemId());
                    tiIds.add(item2.getTransferItemId());
                    log.debug("Searching for audit item " + item2.getTransferItemId());
                    List<TransferItemAudit> auditItems = getTransferItemAudit(tiIds);
                    for (TransferItemAudit tia : auditItems) {
                        log.debug("Audit Item  LastUpdate Date:{}, Disposition Code:{} ", tia.getLastUpdateDt(), tia.getDispositionCode());
                        if (tia.getLastUpdateDt() != null && tia.getLastUpdateDt().getTime() < lastEodDate.getTime()) {
                            foundOneAuditIssue = true;
                            if (!Constants.DISPOSITION_UU97.equals(tia.getDispositionCode())) {
                                log.debug("Audit item " + tia.getTransferItemAuditId() + " matches and was not UU97");
                                prevDispCodeOf97 = false;
                            }
                            // Found the first record since the last EOD, so break out of the for loop
                            break;
                        }
                    }
                    log.debug("Finished verifying UU97 item " + item2.getTransferItemId());

                } catch (Throwable t) {
                    log.error("Error checking audit information for item " + item2.getTransferItemId()
                            + ", the item will remain on the UU97 list.", t);
                }
                if (foundOneAuditIssue && prevDispCodeOf97) {
                    // Only add items where the disposition code was in UU97 prior to the last EOD run.
                    ottUU97List.add(item2);
                } else {
                    log.info("Skipping Transfer Item because previous disposition code was not UU97.  "
                            + "TransferItem_ID = " + item2.getTransferItemId());
                }
            }
        } catch (Throwable t) {
            log.error("Error checking audit information for item ", t);
        }
        // Get final eodList with actual transfer items for return
        if (!Utility.isEmpty(ottUU97List)) {
            Iterator itemItr = ottUU97List.iterator();
            while (itemItr.hasNext()) {
                item = (EodTransferItem) itemItr.next();
                eodList.addAll(getTransferItemAuditRecordsByTransferItemId(item.getTransferItemId()));
            }
        }
        return eodList;
    }
    private List getPreUU97OTTRecords(Date pre97Date) {
        log.info("Get Pre UU97 OTT records");
        List<IEodTransferItem> list = transferItemRepository.getPreUU97OTTRecords(pre97Date);
        List<EodTransferItem> itemList = new ArrayList<>();
        for (IEodTransferItem ti : list) {
            EodTransferItem item = new EodTransferItem();
            item.setTransferItemId(ti.getTransferItemId());
            item.setDispositionCode(ti.getDispositionCode());
            item.setDispositionDt(ti.getDispositionDt());
            itemList.add(item);
        }
        return itemList;
    }

    private List getNon97OTTRecords(Date lastEodDate) {
        log.info("Get Non 97 OTT records");
        List<IEodTransferItem> list = transferItemRepository.getNon97OTTRecords(lastEodDate);
        List<EodTransferItem> itemList = new ArrayList<>();
        for (IEodTransferItem ti : list) {
            EodTransferItem item = new EodTransferItem();
            item.setTransferItemId(ti.getTransferItemId());
            item.setDispositionCode(ti.getDispositionCode());
            item.setDispositionDt(ti.getDispositionDt());
            itemList.add(item);
        }
        return itemList;
    }

    private List<TransferItemAudit> getTransferItemAudit(List transferItemdIds) {
        log.info("Get transfer item audit");
        List<TransferItemAudit> transferItemAudits = transferItemAuditRepository.findByTransferItems(transferItemdIds);
        log.debug("Found audit items " + transferItemAudits.size());
        return transferItemAudits;
    }

    private List<EodTransferItem> getTransferItemAuditRecordsByTransferItemId(Integer transferItemId) {
        //return transferItemAuditRepository.findByTransferItemId(transferItemId);
        List items = null;
        Optional<List<IEodTransferItem>> list = transferItemRepository.getEodItemById(transferItemId);
        items = convertToEodTransferItemDTO(list);
        return items;
    }
}