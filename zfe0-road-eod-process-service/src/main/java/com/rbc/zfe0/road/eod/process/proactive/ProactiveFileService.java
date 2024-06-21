package com.rbc.zfe0.road.eod.process.proactive;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public interface ProactiveFileService {
    public void generateOTTFullStrippedFile(String statusCode) throws ServiceLinkException, JSchException, SftpException;
}
