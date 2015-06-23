package com.sam.yh.crud.exception;

public class LoggingResellerException extends CrudException {

    public LoggingResellerException() {
        super();
    }

    public LoggingResellerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LoggingResellerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoggingResellerException(String message) {
        super(message);
    }

    public LoggingResellerException(Throwable cause) {
        super(cause);
    }

}
