package com.sam.yh.crud.exception;

public class NotAdminException extends CrudException {

    public NotAdminException() {
        super();
    }

    public NotAdminException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotAdminException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAdminException(String message) {
        super(message);
    }

    public NotAdminException(Throwable cause) {
        super(cause);
    }

}
