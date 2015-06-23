package com.sam.yh.req.bean;

public class IllegalReqParamsException extends Exception {

    public IllegalReqParamsException() {
        super();
    }

    public IllegalReqParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IllegalReqParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalReqParamsException(String message) {
        super(message);
    }

    public IllegalReqParamsException(Throwable cause) {
        super(cause);
    }

}
