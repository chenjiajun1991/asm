package com.sam.yh.crud.exception;

public class SmsCodeSendException extends CrudException {

    public SmsCodeSendException() {
        super();
    }

    public SmsCodeSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SmsCodeSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsCodeSendException(String message) {
        super(message);
    }

    public SmsCodeSendException(Throwable cause) {
        super(cause);
    }

}
