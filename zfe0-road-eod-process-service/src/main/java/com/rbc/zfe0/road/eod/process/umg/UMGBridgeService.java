package com.rbc.zfe0.road.eod.process.umg;

import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UMGBridgeService {

    @Value("${ibm.mq.umg-outbound-queue}")
    private String umgOutboundQueue;

    @Autowired
    JmsTemplate jmsTemplate;

    public void sendClosingLetter(String umgMessage) throws ServiceLinkException {
        try {
            String xmlMessage = encodeMessage(umgMessage);
            log.info("Sending Closed Letter");
            jmsTemplate.convertAndSend(umgOutboundQueue, xmlMessage);
            log.info("Finished sending closed Letter to UMG queue:{}", umgOutboundQueue);
        } catch (Throwable e) {
            log.error("Throwable caught while sending closed letter to UMG queue:{}, Error:{}", umgOutboundQueue, e);
            throw new ServiceLinkException(new Exception(e.getMessage()));
        }
    }

    private String encodeMessage(String message) {
        String msg = Utility.encodeForAmpersand(message);
        msg = Utility.encodeForGreaterThan(message);
        msg = Utility.encodeForLessThan(message);
        msg = Utility.encodeForApostrophe(message);
        return msg;
    }

}
