package com.sam.yh.crud.exception;

public class UserSignupException extends CrudException {

    public UserSignupException() {
        super();
    }

    public UserSignupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserSignupException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserSignupException(String message) {
        super(message);
    }

    public UserSignupException(Throwable cause) {
        super(cause);
    }

}
