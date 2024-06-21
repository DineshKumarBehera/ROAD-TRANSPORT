package com.rbc.zfe0.road.eod.exceptions;

public class ServiceLinkException extends Exception {

    public ServiceLinkException(Exception exp) {
//RoadEmailAlert.sendSupportNotification("(EJB) " + exp.getMessage(), exp);
    }

    public ServiceLinkException(Throwable t) {
//RoadEmailAlert.sendSupportNotification("(EJB) " + t.getMessage(), t);
    }

    public ServiceLinkException(String message, Throwable cause) {
//RoadEmailAlert.sendSupportNotification("(EJB) " + message, cause);
    }
}
