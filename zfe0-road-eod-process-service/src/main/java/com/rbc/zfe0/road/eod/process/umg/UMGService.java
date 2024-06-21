package com.rbc.zfe0.road.eod.process.umg;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.rbc.zfe0.road.eod.constants.Constants;
import com.rbc.zfe0.road.eod.exceptions.ServiceLinkException;
import com.rbc.zfe0.road.eod.persistence.entity.TransferItem;
import com.rbc.zfe0.road.eod.process.umg.dto.*;
import com.rbc.zfe0.road.eod.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class UMGService {

    @Autowired
    UMGBridgeService umgBridgeService;

    private static SimpleDateFormat fmt = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private static final String SCON_ID_NORMAL = "WTRFR095";
    private static final String SCON_ID_ESCHEATED = "WTRFR097";
    private static final String SCON_ID_CONFISCATED = "WTRFR096";
    private static final String SCON_ID_WORTHLESS = "WTRFR098";
    private static final String DESCRIPTION_NORMAL = "NORMAL LETTER";
    private static final String DESCRIPTION_ESCHEATED = "ESCHEATED LETTER";
    private static final String DESCRIPTION_CONFISCATED = "CONFISCATED LETTER ";
    private static final String DESCRIPTION_WORTHLESS = "WORTHLESS LETTER";
    private static final String SUBJECT_NORMAL = "CLOSING LETTER NORMAL";
    private static final String SUBJECT_ESCHEATED = "CLOSING LETTER ESCHEATED ";
    private static final String SUBJECT_CONFISCATED = "CLOSING LETTER CONFISCATED ";
    private static final String SUBJECT_WORTHLESS = "CLOSING LETTER WORTHLESS ";

    public void sendWire(TransferItem transferItem) throws ServiceLinkException {
        log.info("Send UMG wire");
        String umgMessage = null;
        try {
            umgMessage = populateClosingLetter(transferItem);
            umgBridgeService.sendClosingLetter(umgMessage);
        } catch (Exception e) {
            throw new ServiceLinkException(e);
        }
    }

    private String populateClosingLetter(TransferItem itemSummary) throws Exception {
        log.info("Populate closing letter");
        if (itemSummary == null) {
            throw new Exception("UMG WIRE could not be created because"
                    + " the item summary object is null");
        }

        String sconId = "";
        String subject = "";
        String description = "";
        String qty = "";
        String repId = itemSummary.getRepId();
        BigDecimal quantity = itemSummary.getOriginalQty();
        String disposition = itemSummary.getDispositionCode();

        if (disposition == null) {
            throw new Exception("UMG WIRE could not be created because"
                    + " the dispoition code is null for transfer item "
                    + itemSummary.getTransferItemId());
        }

        disposition = disposition.trim().toUpperCase();
        // Set up variables...
        if (disposition.equals(Constants.CLOSE_TYPE_NORMAL)) {
            //normal
            sconId = SCON_ID_NORMAL;
            subject = SUBJECT_NORMAL;
            description = DESCRIPTION_NORMAL;
        } else if (disposition.equals(Constants.CLOSE_TYPE_ESCHEATED)) {
            //Escheated
            sconId = SCON_ID_ESCHEATED;
            subject = SUBJECT_ESCHEATED;
            description = DESCRIPTION_ESCHEATED;
        } else if (disposition.equals(Constants.CLOSE_TYPE_CONFISCATED)) {
            //Confiscated
            sconId = SCON_ID_CONFISCATED;
            subject = SUBJECT_CONFISCATED;
            description = DESCRIPTION_CONFISCATED;
        } else if (disposition.equals(Constants.CLOSE_TYPE_WORTHLESS)) {
            //worthless
            sconId = SCON_ID_WORTHLESS;
            subject = SUBJECT_WORTHLESS;
            description = DESCRIPTION_WORTHLESS;
        } else {
            throw new Exception("UMG WIRE could not be created because"
                    + " an unknown disposition type of " + disposition + " was encountered");
        }

        if (repId == null) {
            repId = "";
        }

        if (quantity != null) {
            qty = quantity.toString();
        }

        //don't need to send RR code to UMG as it is always '000'
        if (repId.length() == 6) {
            repId = itemSummary.getRepId().substring(3, repId.length());
        }
        //send 5 digit account number
        String account = itemSummary.getAdpAccountNumber();
        if ((account != null) && (account.length() > 4)) {
            account = itemSummary.getAdpAccountNumber().substring(3,
                    itemSummary.getAdpAccountNumber().length() - 2);
        }
        ADPMessage umgMessage = new ADPMessage();
        umgMessage.setClientNumber(Constants.CLIENT_NUM_2);
        umgMessage.setMessageId(sconId);
        Header header = new Header();
        Recipient recipient = new Recipient();
        recipient.setBranch(itemSummary.getAdpBranchCode());
        recipient.setrR(repId);
        header.setRecipient(recipient);
        header.setSender(Constants.APPNAME);
        header.setSourceProgram(Constants.APPNAME);
        header.setSendTime(fmt.format(new Date()));
        header.setAccount(account);
        header.setAltBranch(itemSummary.getAltBranchCode());
        umgMessage.setHeader(header);
        Body body = new Body();
        Account acct = new Account();
        acct.setBranchCD(itemSummary.getAdpBranchCode());
        acct.setAccountCD(account);
        acct.setAccountName(getRegistrationName(itemSummary.getRegistrationDescr()));
        body.setAccount(acct);
        body.setDescription(description);
        body.setAttention(repId);
        body.setMsgDate(Utility.formatDate(itemSummary.getCloseDt()));
        body.setSecurity(itemSummary.getOriginalAdpSecurityNumber());
        body.setSecurityName(itemSummary.getOriginalSecurityDescr());
        body.setQuantity(qty);
        umgMessage.setBody(body);
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        String umgOutboundMessage = xmlMapper.writeValueAsString(umgMessage);
        log.info("UMG Message: {}", umgOutboundMessage.toString());
        return umgOutboundMessage;
    }

    /**
     * Remove escape characters from string for xml
     *
     * @param registrationDesc
     * @return name
     */
    private String getRegistrationName(String registrationDesc) {
        String name = "";
        int index = 0;
        if (!Utility.isEmpty(registrationDesc)) {
            index = registrationDesc.indexOf('\n');
            if (index > 0) {
                name = registrationDesc.substring(0, index);
            } else {
                name = registrationDesc;
            }
            name = name.replace('\n', ' ');
            name = name.replace('\r', ' ');
        }
        return name;
    }
}