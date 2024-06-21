package com.rbc.zfe0.road.eod.process.aging;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.persistence.entity.Issue;
import com.rbc.zfe0.road.eod.persistence.entity.Note;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.persistence.repository.IssueRepository;
import com.rbc.zfe0.road.eod.persistence.repository.NoteRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class EodAging {

    private static final int AGING_DAYS_25 = -23;
    private static final int AGING_DAYS_30 = -28;
    private static final int AGING_DAYS_35 = -33;
    private static final int AGING_DAYS_40 = -38;
    private static final int AGING_DAYS_41 = -39;
    private static final int AGING_DAYS_75 = -73;
    private static final int AGING_DAYS_85 = -83;
    private static final int AGING_DAYS_90 = -88;
    private static final int AGING_DAYS_91 = -89;

    @Autowired
    TransferItemRepository transferItemRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    IssueRepository issueRepository;

    //call the aging algorithm and update the database
    public List updateDispositionCodes() throws ServiceLinkException {
        List errorList = new ArrayList();
        try {
            log.info("EOD: Running the Aging job and update the  disposition code");
            updateItemsForDisposition(Constants.DISPOSITION_TX25);
            updateItemsForDisposition(Constants.DISPOSITION_TX35);
            updateItemsForDisposition(Constants.DISPOSITION_TX40);
            updateItemsForDisposition(Constants.DISPOSITION_CF75);
            updateItemsForDisposition(Constants.DISPOSITION_CF85);
            updateItemsForDisposition(Constants.DISPOSITION_CF90);
            updateItemsForDisposition(Constants.DISPOSITION_UU97);
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: Aging job exception " + t.getMessage(), t);
            errorList.add(re);
            log.error("EOD process failed while running aging job - ", t);
        }
        return errorList;
    }
    /**
     * Update the disposition for items that now meet the criteria of the disposition passed in.
     * This method will only update transfer items that are not already at the disposition but now
     * meet the criteria.  It will also update the disposition date to the current date.
     * <p>
     * The notifications to be sent can be determined by checking all items which have a
     * disposition date > the confirmation sent date.  When sending notifications, the process
     * must update the confirmation sent date, which will be the indicator that the notification
     * for the change in disposition has been sent.
     *
     * @param dispositionCode The disposition code to update.
     */
    private void updateItemsForDisposition(String dispositionCode) {
        log.info("Update Disposition code: {}", dispositionCode);
        List<TransferItem> transferItems = null;
        Calendar asOfCal = new GregorianCalendar();

        asOfCal.set(Calendar.HOUR_OF_DAY, 23);
        asOfCal.set(Calendar.MINUTE, 59);
        asOfCal.set(Calendar.SECOND, 59);
        asOfCal.set(Calendar.MILLISECOND, 999);

        Calendar futureDateMaxCal = new GregorianCalendar();
        futureDateMaxCal.setTime(asOfCal.getTime());

        Calendar futureDateMinCal = new GregorianCalendar();
        futureDateMinCal.setTime(asOfCal.getTime());

        if (Constants.DISPOSITION_CF00.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_75);
            transferItems = transferItemRepository.getDispositionCF00Query(futureDateMaxCal.getTime(), dispositionCode);
            saveTransferItemAndNote(transferItems, dispositionCode);
            updateFromBox97();

        } else if (Constants.DISPOSITION_CF75.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_75);
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_85);
            transferItems = transferItemRepository.getDispositionCFQuery(futureDateMaxCal.getTime(), futureDateMinCal.getTime(), dispositionCode);
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_CF85.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_85);
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_90);
            transferItems = transferItemRepository.getDispositionCFQuery(futureDateMaxCal.getTime(), futureDateMinCal.getTime(), dispositionCode);
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_CF90.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_90);
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_91);
            transferItems = transferItemRepository.getDispositionCFQuery(futureDateMaxCal.getTime(), futureDateMinCal.getTime(), dispositionCode);
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_TX00.equals(dispositionCode)) {
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_25);
            transferItems = transferItemRepository.getDispositionTX00Query(futureDateMinCal.getTime());
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_TX25.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_25); // search for date less than or equal to this
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_35); // search for date greater than this
            transferItems = transferItemRepository.getDispositionTXQuery(futureDateMaxCal.getTime(), futureDateMinCal.getTime());
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_TX35.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_35); // search for date less than or equal to this
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_40); // search for date greater than this
            transferItems = transferItemRepository.getDispositionTXQuery(futureDateMaxCal.getTime(), futureDateMinCal.getTime());
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_TX40.equals(dispositionCode)) {
            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_40); // search for date less than or equal to this
            futureDateMinCal.add(Calendar.DATE, AGING_DAYS_41); // search for date greater than this
            transferItems = transferItemRepository.getDispositionTXQuery(futureDateMaxCal.getTime(), futureDateMinCal.getTime());
            saveTransferItemAndNote(transferItems, dispositionCode);

        } else if (Constants.DISPOSITION_UU97.equals(dispositionCode)) {
            Calendar futureDateMaxCalConfirmed = new GregorianCalendar();
            futureDateMaxCalConfirmed.setTime(futureDateMaxCal.getTime());

            futureDateMaxCal.add(Calendar.DATE, AGING_DAYS_41); // search for date less than or equal to this
            futureDateMaxCalConfirmed.add(Calendar.DATE, AGING_DAYS_91); // search for date less than or equal to this
            transferItems = transferItemRepository.getDispositionUU97Query(futureDateMaxCal.getTime(), futureDateMaxCalConfirmed.getTime());
            saveTransferItemAndNote(transferItems, dispositionCode);
            updateToBox97();
        }
    }
    private void saveTransferItemAndNote(List<TransferItem> transferItems, String dispositionCode) {
        for (TransferItem ti : transferItems) {
            ti.setDispositionCode(dispositionCode);
            ti.setDispositionDt(new Date());
            ti.setLastUpdateDt(new Date());
            if (!Constants.DISPOSITION_CF00.equals(dispositionCode) &&
                    !Constants.DISPOSITION_TX00.equals(dispositionCode)) {
                ti.setConfirmationSentDt(new Date());
            }
            ti.setLastUpdateName(Constants.APPNAME + "_AGING");
            Note note = new Note();
            note.setTransferItemId(ti.getTransferItemId());
            note.setNoteText("Disposition changed to " + dispositionCode);
            note.setCreatedDate(new Date());
            note.setLastUpdateDate(new Date());
            note.setLastUpdateName(Constants.APPNAME + "_AGING");
            note.setEventCode(Constants.NOTE_EVENT_DISPOSITION_CHANGE);
            note.setStatusCode(ti.getStatusCode());
            transferItemRepository.save(ti);
            noteRepository.save(note);
        }
    }

    private void updateFromBox97() {
        List<Issue> issues = issueRepository.getRecordsUpdateFromBox97Query();
        for (Issue issue : issues) {
            issue.setCseAccountNumber(issue.getDleAccountNumber());
            issue.setCseAccountType(issue.getDleAccountNumber());
            issue.setCseAccountCheckDigit(issue.getDleAccountCheckDigit());
            issue.setDleAccountNumber(issue.getTransferItem().getOriginalCseAccountNumber());
            issue.setDleAccountType(issue.getTransferItem().getOriginalCseAccountType());
            issue.setDleAccountCheckDigit(issue.getTransferItem().getOriginalCseAccountCheckDigit());
            issue.setLastUpdateName(Constants.APPNAME + "_AGING");
            issue.setLastUpdateDt(new Date());
            issueRepository.save(issue);
        }
    }

    private void updateToBox97() {
        List<Issue> issues = issueRepository.getRecordsUpdateToBox97Query();
        for (Issue issue : issues) {
            issue.setDleAccountNumber(issue.getCseAccountNumber());
            issue.setDleAccountType(issue.getCseAccountType());
            issue.setDleAccountCheckDigit(issue.getCseAccountCheckDigit());
            issue.setCseAccountNumber("09000097");
            issue.setCseAccountType("1");
            issue.setDleAccountCheckDigit("3");
            issue.setLastUpdateName(Constants.APPNAME + "_AGING");
            issue.setLastUpdateDt(new Date());
            issueRepository.save(issue);
        }
    }
}