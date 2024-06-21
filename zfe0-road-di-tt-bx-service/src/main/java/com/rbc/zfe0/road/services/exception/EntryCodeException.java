package com.rbc.zfe0.road.services.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.lang.Nullable;

@Slf4j
public class EntryCodeException extends RuntimeException {

    @Nullable
    protected Level getDefaultExceptionLoggingLevel(){
        return null;
    }

    public EntryCodeException(String s) {
        this(null, s);
    }

    public EntryCodeException(Level loggingLevels, String s) {
        super(s);
        Level loggingLevel=loggingLevels;
        if (null == loggingLevel) {
            loggingLevel = getDefaultExceptionLoggingLevel();
        }
        if(Level.WARN.equals(loggingLevel)){
            log.warn(s);
        }else{
            log.error(s);
        }
    }

    public EntryCodeException(Level loggingLevel, String s, Throwable throwable) {
        super(s, throwable);
        if(Level.WARN.equals(loggingLevel)){
            log.warn(s, throwable);
        }else{
            log.error(s, throwable);
        }
    }

    public EntryCodeException(String s, Throwable throwable) {
        this(null, s, throwable);
    }

    public EntryCodeException(Throwable throwable) {
        this(throwable.getLocalizedMessage(), throwable);
    }
}
