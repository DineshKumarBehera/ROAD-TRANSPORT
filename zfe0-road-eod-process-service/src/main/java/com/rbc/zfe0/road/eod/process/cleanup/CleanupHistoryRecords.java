package com.rbc.zfe0.road.eod.process.cleanup;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.persistence.entity.*;
import com.rbc.zfe0.road.eod.persistence.repository.*;
import com.rbc.zfe0.road.eod.process.handler.EodTransit;
import com.rbc.zfe0.road.eod.process.handler.EodUnblockingService;
import com.rbc.zfe0.road.eod.process.handler.mapper.EodTransferItem;
import com.rbc.zfe0.road.eod.process.handler.mapper.IEodTransferItem;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CleanupHistoryRecords {

    @Value("${rbc.road.cleanup.history-delete-months}")
    private String historyDeleteMonths;

    @Autowired
    TransferItemRepository transferItemRepository;

    @Autowired
    TransferItemHistoryRepository transferItemHistoryRepository;

    @Autowired
    NoteHistoryRepository noteHistoryRepository;

    @Autowired
    IssueHistoryRepository issueHistoryRepository;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    EodTransit eodTransit;

    @Autowired
    EodUnblockingService eodUnblockingService;

    private static SimpleDateFormat simpleDateformat = new SimpleDateFormat("MM-dd-yyyy");

    //clear the history and delete the closed transfer items
    public void runCleanup(Date lastEodRundate, List errorList) {
        log.info("run cleanup: clear the history and delete the closed transfer items");
        try {
            Optional<List<IEodTransferItem>> items = transferItemRepository.getEODItemsByStatusCodeAndCloseDt(Constants.STATUS_CLOSED, lastEodRundate);
            List<EodTransferItem> closedItems = eodTransit.convertToEodTransferItemDTO(items);
            for (EodTransferItem eodItem : closedItems) {
                if (simpleDateformat.format(eodItem.getCloseDate()).equalsIgnoreCase(
                        simpleDateformat.format(Utility.getCurrentDate()))) {
                    Optional<IssueHistory> newIssue = issueHistoryRepository.findByIssueId((eodItem.getIssueId()));
                    try {
                        if (newIssue.isPresent()) {
                            newIssue.get().setBatchCode(eodItem.getBatchCode());
                            newIssue.get().setEntryCode(eodItem.getEntryCode());
                            newIssue.get().setLastUpdateDt(new Date());
                            newIssue.get().setLastUpdateName(Constants.APPNAME + "_EOD");
                            issueHistoryRepository.save(newIssue.get());
                        }
                    } catch (Exception ex) {
                        RoadException re = new RoadException("EOD: runCleanup error updating "
                                + "issue history for issue: "
                                + (newIssue.get().getIssueId() == null ? "NULL" : newIssue.get().getIssueId().toString()), ex);
                        errorList.add(re);
                        log.error("Error updating issue history for issue: "
                                + (newIssue.get().getIssueId() == null ? "NULL" : newIssue.get().getIssueId().toString()), ex);
                    }
                }
            }
            Date cleanupDate = Utility.addMontsToCurrentDate(-(Integer.valueOf(historyDeleteMonths)));
            log.debug("EOD Cleaning up, using date " + cleanupDate.toString());
            eodCleanup(cleanupDate);

            // Unblock items and issues that were blocked.
            log.debug("EOD un-blocking all items and issues.");
            eodUnblockingService.eodUnblock();
            log.debug("EOD finished un-blocking all items and issues.");
        } catch (Throwable t) {
            RoadException re = new RoadException("Error in runCleanup " + t.getMessage(), t);
            errorList.add(re);
            log.error("Error while running cleanup routine", t);
        }
    }
    private void eodCleanup(Date cleanupDate) {
        cleanupHistoryRecords(cleanupDate);
        deleteClosedItems();
    }

    private void cleanupHistoryRecords(Date cleanupDate) {
        log.info("clean up history records");
        Optional<List<TransferItemHistory>> tiHistories = transferItemHistoryRepository.findByDispositionDtLessThan(cleanupDate);
        if (tiHistories.isPresent()) {
            for (TransferItemHistory tiHistory : tiHistories.get()) {
                List<NoteHistory> noteHistories = noteHistoryRepository.deleteNoteHistory(tiHistory.getTransferItemHistoryId());
                log.info("Note History clean up records: {}", noteHistories.size());
                if (noteHistories.size() > 0) {
                    noteHistories.forEach(n->{
                        noteHistoryRepository.deleteById(n.getNoteHistoryId());
                    });
                }
                List<IssueHistory> issueHistories = issueHistoryRepository.deleteIssueHistory(tiHistory.getTransferItemHistoryId());
                log.info("Issue History clean up records: {}", issueHistories.size());
                if(issueHistories.size()>0){
                    issueHistories.forEach(i->{
                        issueHistoryRepository.deleteById(i.getIssueHistoryId());
                    });
                }
                List<TransferItemHistory> transferItemHistories = transferItemHistoryRepository.deleteTransferItemHistory(tiHistory.getTransferItemHistoryId());
                if(transferItemHistories.size()>0){
                    transferItemHistories.forEach(ti->{
                        transferItemHistoryRepository.deleteById(ti.getTransferItemHistoryId());
                    });
                }
                log.info("Transfer Item History clean up records: {}", transferItemHistories.size());
            }
        }
    }

    private void deleteClosedItems() {
        log.info("delete closed item");
        List<Issue> closedIssues = issueRepository.deleteClosedIssues();
        log.info("Closed Issue clean up record(s): {}", closedIssues.size());
        if (closedIssues.size() > 0) {
            closedIssues.forEach(i -> {
                issueRepository.deleteById(i.getIssueID());
            });
        }
        List<TransferItem> closedItems = transferItemRepository.deleteClosedTransferItem();
        log.info("Closed Transfer Item clean up record(s): {}", closedItems.size());
        if (closedItems.size() > 0) {
            closedItems.forEach(ti -> {
                transferItemRepository.deleteById(ti.getTransferItemId());
            });
        }
    }

}