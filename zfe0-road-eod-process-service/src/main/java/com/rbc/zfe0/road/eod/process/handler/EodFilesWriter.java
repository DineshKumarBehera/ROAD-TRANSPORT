package com.rbc.zfe0.road.eod.process.handler;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class EodFilesWriter {

    @Value("${rbc.road.scheduler.eod.eod-file-path}")
    private String eodFilePath;

    @Value("${rbc.road.scheduler.eod.report-log-file-path}")
    private String reportLogFilePath;

    private static SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-yyyy");

    @Autowired
    EodFileTransfer eodFileTransfer;

    public void writeICRReport(List recs) throws ServiceLinkException, SftpException, JSchException {
        log.info("Write ICR report");
        String fileName = eodFilePath + Constants.ICR_REPORT_FILE_NAME + Constants.FILE_EXTENSION;
        writeFile(fileName, recs, false);
        eodFileTransfer.uploadFile(fileName);
        //      take a backup
        fileName = reportLogFilePath +
                Constants.ICR_REPORT_FILE_NAME + "-" + fmt.format((new Date())) + Constants.FILE_EXTENSION;
        writeFile(fileName, recs, false);
    }

    public void writeCAGE(List recs) throws ServiceLinkException, SftpException, JSchException {
        log.info("Write CAGE report");
        String fileName = eodFilePath + Constants.CAGE_FILE_NAME + Constants.FILE_EXTENSION;
        writeFile(fileName, recs, true);
        eodFileTransfer.uploadFile(fileName);
        //take a backup
        fileName = reportLogFilePath +
                Constants.CAGE_FILE_NAME + "-" + fmt.format((new Date())) + Constants.FILE_EXTENSION;
        writeFile(fileName, recs, false);

    }


    //IntactLog file
    public void wrtieINTACTLog(List recs) throws ServiceLinkException, SftpException, JSchException {
        log.info("Write INTACT log.");
        String fileName = reportLogFilePath +
                Constants.INTACT_LOG_FILE_NAME + fmt.format((new Date()))+ Constants.INTACT_LOG_FILE_EXTENSION;
        writeFile(fileName, recs, false);
    }

    //write INTACT file
    public void writeINTACT(List recs) throws ServiceLinkException, SftpException, JSchException {
        log.info("Write INTACT file");
        String fileName = eodFilePath + Constants.INTACT_FILE_NAME + Constants.FILE_EXTENSION;
        writeFile(fileName, recs, false);
        eodFileTransfer.uploadFile(fileName);
        //      take a backup
        fileName = reportLogFilePath +
                Constants.INTACT_FILE_NAME + "-"+fmt.format((new Date()))+ Constants.FILE_EXTENSION;
        writeFile(fileName, recs, false);
    }

    public void writeControlFile() throws ServiceLinkException, SftpException, JSchException {
        log.info("Write Control File");
        List recs = new ArrayList();
        String fileName = eodFilePath + Constants.CONTROL_FILE_NAME;
        writeFile(fileName, recs , false);
        eodFileTransfer.uploadFile(fileName);
    }

    private void writeFile(String fileName, List recs, boolean isDateHeader) throws ServiceLinkException {
        Iterator itr = recs.iterator();
        String rec = null;
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(fileName));
            if (isDateHeader) {
                out.write(getDateHeader());
                out.write("\n");
            }
            while (itr.hasNext()) {
                rec = (String) itr.next();
                out.write(rec);
                out.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceLinkException(e);
        } finally {
            try {
                out.close();
            } catch (IOException exp) {
                throw new ServiceLinkException(exp);
            }
        }
    }

    private String getDateHeader() {
        return fmt.format(new Date());
    }
}
