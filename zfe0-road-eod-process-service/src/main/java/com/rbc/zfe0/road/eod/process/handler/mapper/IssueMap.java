package com.rbc.zfe0.road.eod.process.handler.mapper;

import java.util.ArrayList;

/** Class to store issues to create a relation with parent transfer item
 *
 */
public class IssueMap {

    public ArrayList getCashIssues() {
        return cashIssues;
    }

    public void setCashIssues(ArrayList cashIssues) {
        this.cashIssues = cashIssues;
    }

    public ArrayList getSecurityIssue() {
        return securityIssue;
    }

    public void setSecurityIssue(ArrayList securityIssue) {
        this.securityIssue = securityIssue;
    }

    private ArrayList cashIssues;
    private ArrayList securityIssue;

    public IssueMap(){
        cashIssues = new ArrayList();
        securityIssue = new ArrayList();
    }
}
