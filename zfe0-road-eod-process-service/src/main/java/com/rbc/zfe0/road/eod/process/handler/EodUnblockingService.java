
package com.rbc.zfe0.road.eod.process.handler;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.persistence.entity.*;
import com.rbc.zfe0.road.eod.persistence.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@Transactional
public class EodUnblockingService {

    private final IssueRepository issueRepository;

    private final NoteRepository noteRepository;

    private final TransferItemHistoryRepository transferItemHistoryRepository;

    private final NoteHistoryRepository noteHistoryRepository;

    private final TransferItemRepository transferItemRepository;

    @Autowired
    public EodUnblockingService(IssueRepository issueRepository, NoteRepository noteRepository, TransferItemHistoryRepository transferItemHistoryRepository, NoteHistoryRepository noteHistoryRepository, TransferItemRepository transferItemRepository) {
        this.issueRepository = issueRepository;
        this.noteRepository = noteRepository;
        this.transferItemHistoryRepository = transferItemHistoryRepository;
        this.noteHistoryRepository = noteHistoryRepository;
        this.transferItemRepository = transferItemRepository;
    }

    public void eodUnblock() {
        log.info("Unblocking all entries in EOD");
        unblockNewIssue();
        unblockOriginalIssue();
    }

    private void unblockNewIssue() {
        log.info("Unblock all new issue");
        List<Issue> issues = issueRepository.findByBlockFlag(1);
        if (issues != null && issues.size() > 0) {
            log.debug("EOD - unblocking " + issues.size() + " new issues");
            for (Issue issue : issues) {
                log.debug("EOD - unblocking new issue transferitem_id=" + issue.getTransferItem().getTransferItemId()
                        + ", issue_id=" + issue.getIssueID());
                issue.setBlockFlag(0);
                Note note = new Note();
                note.setTransferItemId(issue.getTransferItem().getTransferItemId());
                note.setStatusCode(issue.getTransferItem().getStatusCode());
                note.setNoteText("New Issue Unblocked, ID = " + issue.getIssueID());
                note.setEventCode(Constants.NOTE_EVENT_ITEM_UNBLOCKED);
                note.setCreatedDate(new Date());
                note.setLastUpdateDate(new Date());
                note.setLastUpdateName("ROADSVC");
                issueRepository.save(issue);
                noteRepository.save(note);

                if (issue.getTransferItem().getStatusCode().equals(Constants.STATUS_CLOSED)) {
                    Optional<TransferItemHistory> transferItemHistory = transferItemHistoryRepository.findByTransferItemId(issue.getTransferItem().getTransferItemId());
                    if (transferItemHistory.isPresent()) {
                        NoteHistory noteHistory = new NoteHistory();
                        noteHistory.setTransferItemHistoryId(transferItemHistory.get().getTransferItemHistoryId());
                        noteHistory.setStatusCode(transferItemHistory.get().getStatusCode());
                        noteHistory.setNote("New Issue Unblocked, ID = " + issue.getIssueID());
                        noteHistory.setEventCode(Constants.NOTE_EVENT_ITEM_UNBLOCKED);
                        noteHistory.setLastUpdateDt(new Date());
                        noteHistory.setLastUpdateName("ROADSVC");
                        noteHistoryRepository.save(noteHistory);
                    }
                }
            }
        }
    }

    private void unblockOriginalIssue() {
        log.info("Unblock all original issue");
        Optional<List<TransferItem>> transferItems = transferItemRepository.findByBlockFlag(1);
        if (transferItems.isPresent()) {
            log.debug("EOD - unblocking " + transferItems.get().size() + " original issues");
            for (TransferItem transferItem : transferItems.get()) {
                transferItem.setBlockFlag(0);
                Note note = new Note();
                note.setTransferItemId(transferItem.getTransferItemId());
                note.setStatusCode(transferItem.getStatusCode());
                note.setNoteText("Original Issue Unblocked");
                note.setEventCode(Constants.NOTE_EVENT_ITEM_UNBLOCKED);
                note.setCreatedDate(new Date());
                note.setLastUpdateDate(new Date());
                note.setLastUpdateName("ROADSVC");
                transferItemRepository.save(transferItem);
                noteRepository.save(note);

                if (transferItem.getStatusCode().equals(Constants.STATUS_CLOSED)) {
                    Optional<TransferItemHistory> transferItemHistory = transferItemHistoryRepository.findByTransferItemId(transferItem.getTransferItemId());
                    if (transferItemHistory.isPresent()) {
                        NoteHistory noteHistory = new NoteHistory();
                        noteHistory.setTransferItemHistoryId(transferItemHistory.get().getTransferItemHistoryId());
                        noteHistory.setStatusCode(transferItemHistory.get().getStatusCode());
                        noteHistory.setNote("Original Issue Unblocked");
                        noteHistory.setEventCode(Constants.NOTE_EVENT_ITEM_UNBLOCKED);
                        noteHistory.setLastUpdateDt(new Date());
                        noteHistory.setLastUpdateName("ROADSVC");
                        noteHistoryRepository.save(noteHistory);
                    }
                }
            }
        }
    }
}



