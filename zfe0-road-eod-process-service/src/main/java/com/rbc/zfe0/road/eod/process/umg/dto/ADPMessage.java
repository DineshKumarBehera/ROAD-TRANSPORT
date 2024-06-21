package com.rbc.zfe0.road.eod.process.umg.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ADP_Message")
public class ADPMessage {
    @JacksonXmlProperty(isAttribute = true, localName = "Client_Number")
    private String clientNumber;
    @JacksonXmlProperty(isAttribute = true, localName = "Message_id")
    private String messageId;
    @JacksonXmlProperty(localName = "Header")
    private Header header;
    @JacksonXmlProperty(localName = "Body")
    private Body body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(String clientNumber) {
        this.clientNumber = clientNumber;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}
