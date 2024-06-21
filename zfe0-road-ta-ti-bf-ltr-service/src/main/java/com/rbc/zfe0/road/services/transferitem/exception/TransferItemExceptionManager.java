package com.rbc.zfe0.road.services.transferitem.exception;

import com.rbc.zfe0.road.services.transferitem.dto.ErrorInfoDto;

public class TransferItemExceptionManager extends Exception {

    private static final long serialVersionUID = -1896753911925541392L;
    protected ErrorInfoDto errorInfoDto;

    public TransferItemExceptionManager(String message, int errorCode, String errorMessage) {
        super();
        errorInfoDto = fromErrorInfo(errorCode,errorMessage);
    }

    public TransferItemExceptionManager(String message, Exception e) {
    }


    private ErrorInfoDto fromErrorInfo(int errorCode, String errorMessage) {
        ErrorInfoDto errorInfoDto = null;
        errorInfoDto = new ErrorInfoDto();
        errorInfoDto.setErrorCode(errorCode);
        errorInfoDto.setErrorMessage(errorMessage);
        return errorInfoDto;
    }

    public ErrorInfoDto getErrorInfo() {
        return errorInfoDto;
    }
}
