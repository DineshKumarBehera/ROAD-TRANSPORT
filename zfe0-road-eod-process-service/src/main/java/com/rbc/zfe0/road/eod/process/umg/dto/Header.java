package com.rbc.zfe0.road.eod.process.umg.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Header {
    @JacksonXmlProperty(localName = "Sender")
    private String sender;
    @JacksonXmlProperty(localName = "SendTime")
    private String sendTime;
    @JacksonXmlProperty(localName = "Account")
    private String account;
    @JacksonXmlProperty(localName = "SourceProgram")
    private String sourceProgram;
    @JacksonXmlProperty(localName = "Recipient")
    private Recipient recipient;
    @JacksonXmlProperty(localName = "AltBranch")
    private String altBranch;
    @JacksonXmlProperty(localName = "Subject")
    private String subject;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSourceProgram() {
        return sourceProgram;
    }

    public void setSourceProgram(String sourceProgram) {
        this.sourceProgram = sourceProgram;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public String getAltBranch() {
        return altBranch;
    }

    public void setAltBranch(String altBranch) {
        this.altBranch = altBranch;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
