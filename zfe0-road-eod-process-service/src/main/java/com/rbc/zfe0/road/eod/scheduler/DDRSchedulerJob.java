package com.rbc.zfe0.road.eod.scheduler;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;

import com.rbc.zfe0.road.eod.persistence.repository.TransferItemRepository;
import com.rbc.zfe0.road.eod.process.handler.EodFileTransfer;
import com.rbc.zfe0.road.eod.utils.HolidayUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@EnableScheduling
public class DDRSchedulerJob {

    @Autowired
    HolidayUtil holidayUtil;

    @Value("${rbc.road.scheduler.ddr.run-ddr-scheduler}")
    private Boolean runEodScheduler;

    @Autowired
    private TransferItemRepository ddrJobSchedulerRepository;

    @Value("${rbc.road.scheduler.ddr.ddr-file-path}")
    private String reportFilePath;

    @Value("${rbc.road.scheduler.ddr.run-ddr-scheduler}")
    private Boolean runDDRScheduler;

    @Autowired
    EodFileTransfer eodFileTransfer;

    private static final String[] COLUMN_NAMES = {"TRANSFERITEM_ID", "TRANSFERREDDT", "TRANSFERTYPECODE",
            "STATUSCODE", "ADPACCOUNTNUMBER", "ORIGINALSECURITYDESCR", "ORIGINALADPSECURITYNUMBER","ORIGINALQTY","BATCH_RUN_DATE"};
    private static final int COLUMN_WIDTH = 35;

    @Scheduled(cron = "${rbc.road.scheduler.ddr.cron-expression:}", zone = "${rbc.road.scheduler.ddr.time-zone:}")
    @Transactional
    public void runDDRJobs() throws JSchException, SftpException {
        String reportFile = reportFilePath + Constants.DDR_FILE_INPUT_NAME + Constants.FILE_EXTENSION;
        // Convert holidayList to a List of LocalDate objects using Stream API
//        List<LocalDate> dates = holidayList.stream().map(LocalDate::parse).collect(Collectors.toList());
        // Check if current date is a holiday
        LocalDate today = LocalDate.now();
        boolean isHoliday = holidayUtil.isHoliday();
        if (isHoliday) {
            log.info("Today is a holiday.. No report will be generated today.");
            System.out.println("Today is a holiday: " + today);
            return;
        }
        System.out.println("Creating report for today: " + today);
        // call the createData() method here with appropriate parameters
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            log.info("Executing report generation job");
            List<TransferItem> findTransferItemDetails = ddrJobSchedulerRepository.findByTransferTypeCode("TTSE");
            log.info("Creating file: {}", reportFile);
            // Write column names to output file
            writeData(COLUMN_NAMES, writer, true);

            for (TransferItem item : findTransferItemDetails) {
                writeData(convertTransferItemToMap(item), writer, false);
            }

            log.info("Report generated successfully");

            log.info("File sent to sftp successfully.");

        } catch (IOException e) {
            log.error("Error occurred while writing report to file", e);
            e.printStackTrace();
        }
        eodFileTransfer.uploadFile(reportFile);
    }


    private static Map<String, String> convertTransferItemToMap(TransferItem item) {
        LocalDate today = LocalDate.now();
        Map<String, String> arguments = new LinkedHashMap<>();
        arguments.put("TRANSFERITEM_ID", String.valueOf(item.getTransferItemId()));
        arguments.put("TRANSFERREDDT", String.valueOf(item.getTransferredDt()));
        arguments.put("TRANSFERTYPECODE", String.valueOf(item.getTransferType().getTransferTypeCode()));
        arguments.put("STATUSCODE", String.valueOf(item.getStatusCode()));
        arguments.put("ADPACCOUNTNUMBER", String.valueOf(item.getAdpAccountNumber()));
        arguments.put("ORIGINALSECURITYDESCR", String.valueOf(item.getOriginalSecurityDescr()));
        arguments.put("ORIGINALADPSECURITYNUMBER", String.valueOf(item.getOriginalAdpSecurityNumber()));
        arguments.put("ORIGINALQTY", String.valueOf(item.getOriginalQty()));
        arguments.put("BATCH_RUN_DATE", today.toString());
        return arguments;
    }

    private static void writeData(String[] data, BufferedWriter writer, boolean isHeader) throws IOException {
        // Write column names
        for (int i = 0; i < data.length; i++) {
            String value = pad(data[i], COLUMN_WIDTH);
            writer.write(value);
            if (i != data.length - 1) {
                writer.write("|");
            }
        }
        writer.newLine();
    }

    //Data is written here
    private static void writeData(Map<String, String> data, BufferedWriter writer, boolean isHeader)
            throws IOException {
        // Write data in column order
        for (String columnName : COLUMN_NAMES) {
            String value = data.get(columnName);
            if (!columnName.equalsIgnoreCase("BATCH_RUN_DATE")) {
                String paddedValue = pad(value == null ? "null" : value, COLUMN_WIDTH);
                writer.write(paddedValue);
                writer.write("|");
            }
            if (columnName.equalsIgnoreCase("BATCH_RUN_DATE")) {
                if (value != null && !value.equalsIgnoreCase("")) {
                    writer.write(value);
                } else {
                    writer.write("null");
                }
            }
        }
        writer.newLine();


//        if (isHeader) {
//            // Write separator
//            for (int i = 0; i < COLUMN_NAMES.length; i++) {
//                String separator = pad("", COLUMN_WIDTH).replaceAll("\\s", "-");
//                writer.write(separator);
//                if (i != COLUMN_NAMES.length - 1) {
//                    writer.write("+");
//                }
//            }
//            writer.newLine();
//        }
    }

    public static String pad(String value, int width) {
        String padded = String.format("%-" + width + "s", value);
        return padded;
    }

}
