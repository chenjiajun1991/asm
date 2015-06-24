package com.sam.yh.common;

public class IllegalParamsException extends Exception {

    public IllegalParamsException() {
        super();
    }

    public IllegalParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IllegalParamsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalParamsException(String message) {
        super(message);
    }

    public IllegalParamsException(Throwable cause) {
        super(cause);
    }

}
