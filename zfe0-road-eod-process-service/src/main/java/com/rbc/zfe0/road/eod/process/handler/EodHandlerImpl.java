package com.rbc.zfe0.road.eod.process.handler;

import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.exceptions.EndOfDayException;
import com.rbc.zfe0.road.eod.exceptions.RoadException;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.persistence.entity.RegistrationInfo;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.process.aging.EodAging;
import com.rbc.zfe0.road.eod.process.cage.CageGenerator;
import com.rbc.zfe0.road.eod.process.cleanup.CleanupHistoryRecords;
import com.rbc.zfe0.road.eod.process.cleanup.CleanupTransferAgents;
import com.rbc.zfe0.road.eod.process.icr.IcrReportGenerator;
import com.rbc.zfe0.road.eod.process.intact.IntactGenerator;
import com.rbc.zfe0.road.eod.process.umg.UMGService;
import com.rbc.zfe0.road.eod.persistence.repository.RegistrationInfoRepository;
import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class EodHandlerImpl implements EodHandler {

    @Autowired
    RegistrationInfoRepository registrationInfoRepository;

    @Autowired
    TransferItemRepository transferItemRepository;

    @Autowired
    UMGService umgService;

    @Autowired
    IcrReportGenerator icrReportService;

    @Autowired
    CageGenerator cageGeneratorService;

    @Autowired
    IntactGenerator intactGeneratorService;

    @Autowired
    CleanupHistoryRecords cleanupHistoryRecords;

    @Autowired
    CleanupTransferAgents cleanupTransferAgents;

    @Autowired
    EodFilesWriter eodFilesWriter;

    @Autowired
    EodAging eodAging;

    public void runEod() throws RoadException {
        List<String> errorList = new ArrayList<>();
        Date lastEodDate = null;
        try {
            log.debug("EOD processing started.");
            lastEodDate = getLastEodDate();
            errorList.addAll(eodAging.updateDispositionCodes());

            setStartEodDate();

            errorList.addAll(eodMainTransaction(lastEodDate));
            try {
                errorList.addAll(eodNotificationAndCleanup(lastEodDate));
            } catch (Throwable t) {
                log.error("Exception in cleanup", t);
                new EndOfDayException(new Exception("Exception in cleanup", t));
            }

            updateEODDate();

            log.info("EOD PROCESSING: Finished End of day (EOD) processing");
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD PROCESSING: CRITICAL ERROR ENCOUNTERED - "
                    + " Unable to complete end of day (EOD) processing."
                    + " EOD FINISH DATE WAS NOT UPDATED IN THE DATABASE", t);
            // Log for the eod log file
            log.error("EOD PROCESSING: Critical Error encountered.", t);
            log.debug(re.getMessage());
            throw re;
        } finally {
            try {
                printErrorList(errorList);
            } catch (Throwable ignore) {
            }
            // Log for the eod log file
            log.debug("EOD Processing completed ("
                    + errorList.size() + " issues).");
        }
    }

    public List eodMainTransaction(Date lastEodDate) throws RoadException {
        List errorList = new ArrayList();
        try {
            cageGeneratorService.generateCageRecords(lastEodDate, errorList);
            intactGeneratorService.generateIntactFile(lastEodDate, errorList);
            eodFilesWriter.writeControlFile();
        } catch (Throwable t) {
            throw new RoadException("EOD: Errors encountered while running"
                    + " Cage and Intact jobs.  See log.", t);
        }
        return errorList;

    }

    /**
     * get Last EOD run date
     *
     * @return eodEndDate
     * @throws ServiceLinkException
     */
    private Date getLastEodDate() throws ServiceLinkException {
        Date eodEndDate = null;
        Optional<RegistrationInfo> regInfo = registrationInfoRepository.findById(Constants.REGISTRATION_INFO_KEY);
        if (regInfo.isPresent()) {
            eodEndDate = regInfo.get().getEodEndDt();
            log.info("Get last EOD Date: {}", eodEndDate);
        }
        return eodEndDate;
    }
    private List eodNotificationAndCleanup(Date lastEodDate) throws RoadException {
        List errorList = new ArrayList();
        try {
            //TODO: Write logic for sendNotifications(lastEodDate, errorList);
            sendUMGWires(lastEodDate, errorList);
            generateICRReport(errorList);
            deleteTransferAgents(errorList);
            runCleanUp(lastEodDate, errorList);
        } catch (Throwable t) {
            new EndOfDayException("EOD: Errors encountered while running"
                    + " end-of-day cleanup.  See log.", t);
        }
        return errorList;
    }

    private void deleteTransferAgents(List errorList) {
        try {
            cleanupTransferAgents.deleteTransferAgents(errorList);
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: deleteTransferAgents exception "
                    + t.getMessage(), t);
            errorList.add(re);
            log.error("Unable to deleteTransferAgents - ", t);
        }

    }

    /*
     * This method is for getting TransferItem summary records and
     * sends UMG messages for closed items with Normal, Worthless, Escheated, Confiscated.
     */
    private void sendUMGWires(Date lastEodDate, List errorList) {
        try {
            log.info("Sends UMG messages for closed items with Normal, Worthless, Escheated, Confiscated.");
            Optional<List<TransferItem>> transferItems = transferItemRepository.findByStatusCodeAndCloseDtGreaterThan(Constants.STATUS_CLOSED, lastEodDate);
            if (transferItems.isEmpty() || transferItems.get().isEmpty()) {
                log.info("TransferItems with Closed Date:{} are not available for sending UMG wires.", lastEodDate);
                return;
            }
            try {
                for (TransferItem transferItem : transferItems.get()) {
                    log.info("Transfer Item id: {}", transferItem.getTransferItemId());
                    if (!transferItem.getDispositionCode().equalsIgnoreCase(Constants.CLOSE_TYPE_REJECTED)) {
                        log.info("Send Closed letter for Transfer Item {}", transferItem.getTransferItemId());
                        umgService.sendWire(transferItem);
                    }
                }
            } catch (Exception e) {
                RoadException re = new RoadException("EOD: sendUMGWires process failed"
                        + " while sending UMG wires " + e.getMessage(), e);
                errorList.add(re);
                log.warn("EOD process failed while sending UMG wires - ", e);
            }
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: sendUMGWires exception " + t.getMessage(), t);
            errorList.add(re);
            log.error("EOD process failed while sending UMG wires - ", t);
        }

    }

    private void generateICRReport(List errorList) {
        try {
            icrReportService.generateReport();
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: generateICRReport exception "
                    + t.getMessage(), t);
            errorList.add(re);
            log.error("Unable to generate ICR Report - ", t);
        }

    }

    private void runCleanUp(Date lastEodDate, List errorList) {
        log.info("Run Clean up job");
        try {
            cleanupHistoryRecords.runCleanup(lastEodDate, errorList);
        } catch (Throwable t) {
            RoadException re = new RoadException("EOD: runCleanUp exception "
                    + t.getMessage(), t);
            errorList.add(re);
            log.error("Unable to cleanUp - ", t);
        }
    }
    /**
     * Print a list of errors encountered during the end of day process.
     *
     * @param errorList
     */
    private void printErrorList(List errorList) {
        try {
            log.debug("Found " + errorList.size() + " errors.");
            if (errorList.size() > 0) {
                StringBuffer message = new StringBuffer();
                message.append("EOD: Errors encountered: ");
                for (int i = 0; i < errorList.size(); i++) {
                    message.append("\nERROR(" + i + " of " + errorList.size() + ")");
                    RoadException re = (RoadException) errorList.get(i);
                    message.append(re.getMessage());
                }
                log.error(message.toString());
                // Create the notification exception.  No need to throw it - the creation
                // of the exception takes care of the notifications.
                new EndOfDayException(new Exception(message.toString()));
            }
        } catch (Throwable t) {
            log.error("EOD: Unable to create log entries for error", t);
            new EndOfDayException(new Exception("EOD: Unable to create log entries for error"));
        }
    }

    private void setStartEodDate() {
        log.info("Set Start Eod Date");
        Optional<RegistrationInfo> regInfo = registrationInfoRepository.findById(Constants.REGISTRATION_INFO_KEY);
        if (regInfo.isPresent()) {
            regInfo.get().setEodStartDt(new Date());
            regInfo.get().setLastUpdateDt(new Date());
            regInfo.get().setLastUpdateName("ROADSVC");
            registrationInfoRepository.save(regInfo.get());
        }
    }

    private void updateEODDate() {
        log.info("Update EOD date");
        Optional<RegistrationInfo> regInfo = registrationInfoRepository.findById(Constants.REGISTRATION_INFO_KEY);
        if (regInfo.isPresent()) {
            regInfo.get().setEodEndDt(new Date());
            regInfo.get().setLastUpdateDt(new Date());
            regInfo.get().setLastUpdateName("ROADSVC");
            registrationInfoRepository.save(regInfo.get());
        }
    }

}