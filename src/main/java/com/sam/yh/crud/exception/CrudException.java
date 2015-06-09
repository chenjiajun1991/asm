package com.sam.yh.crud.exception;

public class CrudException extends Exception {

    public CrudException() {
        super();
    }

    public CrudException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CrudException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrudException(String message) {
        super(message);
    }

    public CrudException(Throwable cause) {
        super(cause);
    }

}
