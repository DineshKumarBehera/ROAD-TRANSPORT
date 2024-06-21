package com.rbc.zfe0.road.eod.process.umg.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Recipient {

    @JacksonXmlProperty(localName = "RR")
    private String rR;
    @JacksonXmlProperty(localName = "Branch")
    private String branch;

    public String getrR() {
        return rR;
    }

    public void setrR(String rR) {
        this.rR = rR;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
