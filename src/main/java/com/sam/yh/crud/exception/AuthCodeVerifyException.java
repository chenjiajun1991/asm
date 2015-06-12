package com.sam.yh.crud.exception;

public class AuthCodeVerifyException extends CrudException {

    public AuthCodeVerifyException() {
        super();
    }

    public AuthCodeVerifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AuthCodeVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthCodeVerifyException(String message) {
        super(message);
    }

    public AuthCodeVerifyException(Throwable cause) {
        super(cause);
    }

}
