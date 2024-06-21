package com.rbc.zfe0.road.eod.process.umg.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Account {

    @JacksonXmlProperty(localName = "BranchCD")
    private String branchCD;
    @JacksonXmlProperty(localName = "AccountCD")
    private String accountCD;
    @JacksonXmlProperty(localName = "AccountName")
    private String accountName;

    public String getBranchCD() {
        return branchCD;
    }

    public void setBranchCD(String branchCD) {
        this.branchCD = branchCD;
    }

    public String getAccountCD() {
        return accountCD;
    }

    public void setAccountCD(String accountCD) {
        this.accountCD = accountCD;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
