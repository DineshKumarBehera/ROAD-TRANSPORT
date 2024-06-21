package com.rbc.zfe0.road.eod.process.handler;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EodFileTransfer {

    @Value("${rbc.road.sftp.host}")
    private String host;

    @Value("${rbc.road.sftp.username}")
    private String username;

    @Value("${rbc.road.sftp.password}")
    private String password;

    @Value("${rbc.road.sftp.remote-file-path}")
    private String remoteFilePath;


    private ChannelSftp connectSftp() throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        return (ChannelSftp) session.openChannel("sftp");
    }


    public void uploadFile(String localFile) throws JSchException, SftpException {
        ChannelSftp channelSftp = connectSftp();
        try {
            channelSftp.connect();
            channelSftp.put(localFile, remoteFilePath);
            log.info("Successfully sent {} file to SFTP sever", localFile);
        } catch (Exception e) {
            log.error("Unexpected error occurred while sending the {} file to SFTP server {}", localFile, e.getMessage());
        } finally {
            channelSftp.exit();
        }
    }
}
