package com.sam.yh.crud.exception;

public class BtyLockException extends CrudException {

    public BtyLockException() {
        super();
    }

    public BtyLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BtyLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public BtyLockException(String message) {
        super(message);
    }

    public BtyLockException(Throwable cause) {
        super(cause);
    }

}
