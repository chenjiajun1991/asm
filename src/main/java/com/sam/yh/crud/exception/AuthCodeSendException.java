package com.sam.yh.crud.exception;

public class AuthCodeSendException extends CrudException {

    public AuthCodeSendException() {
        super();
    }

    public AuthCodeSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AuthCodeSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthCodeSendException(String message) {
        super(message);
    }

    public AuthCodeSendException(Throwable cause) {
        super(cause);
    }

}
