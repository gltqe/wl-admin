package com.gltqe.wladmin.commons.exception;


public class QuartzException extends RuntimeException {

    public QuartzException() {
        super();
    }

    public QuartzException(String message) {
        super(message);
    }

    public QuartzException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuartzException(Throwable cause) {
        super(cause);
    }

    protected QuartzException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
