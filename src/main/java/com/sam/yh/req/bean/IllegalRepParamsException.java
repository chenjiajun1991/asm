package com.sam.yh.req.bean;

public class IllegalRepParamsException extends Exception {

    public IllegalRepParamsException() {
        super();
    }

    public IllegalRepParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IllegalRepParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRepParamsException(String message) {
        super(message);
    }

    public IllegalRepParamsException(Throwable cause) {
        super(cause);
    }

}
