package com.rbc.zfe0.road.services.transferitem.exception;

public class TransferItemNotFoundException extends TransferItemExceptionManager {

    private static final long serialVersionUID = 5306218552056091886L;


    public TransferItemNotFoundException(String message, int errorCode, String errorMessage) {
        super(message, errorCode, errorMessage);
    }
}
