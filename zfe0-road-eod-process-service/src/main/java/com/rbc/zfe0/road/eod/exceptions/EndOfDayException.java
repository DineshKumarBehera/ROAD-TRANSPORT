package com.rbc.zfe0.road.eod.exceptions;

public class EndOfDayException extends Exception {

    public EndOfDayException(Throwable exp) {
//RoadEmailAlert.sendSupportNotification("EOD: " + exp.getMessage(), exp);
    }

    public EndOfDayException(String message, Throwable cause) {
//RoadEmailAlert.sendSupportNotification("EOD: " + message, cause);
    }
}