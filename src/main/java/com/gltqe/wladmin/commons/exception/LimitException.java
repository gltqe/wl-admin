package com.gltqe.wladmin.commons.exception;

/**
 * 限流
 *
 * @author gltqe
 * @date 2022/7/3 0:37
 **/
public class LimitException extends RuntimeException {
    public LimitException() {
        super();
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }

    protected LimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
