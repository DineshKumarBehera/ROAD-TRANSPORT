package com.rbc.zfe0.road.services.dto.email;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class EmailRequest {

    private String subject;
    private String sendFrom;
    private String sendTo;
    private String content;
    private List<MultipartFile> files;
    private boolean htmlFormat = false;

    public boolean isAttachment() {
        return files != null && !files.isEmpty();
    }

    public boolean isHtmlFormat() {
        return htmlFormat;
    }

    public void setHtmlFormat(boolean htmlFormat) {
        this.htmlFormat = htmlFormat;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
