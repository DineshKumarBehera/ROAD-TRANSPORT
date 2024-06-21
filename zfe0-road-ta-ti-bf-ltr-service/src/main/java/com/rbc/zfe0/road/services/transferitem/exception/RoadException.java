package com.rbc.zfe0.road.services.transferitem.exception;

public class RoadException extends Throwable {

    /**
     *
     */
    public RoadException() {
        super();
    }

    /**
     * @param message
     */
    public RoadException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public RoadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public RoadException(Throwable cause) {
        super(cause);
    }

}
