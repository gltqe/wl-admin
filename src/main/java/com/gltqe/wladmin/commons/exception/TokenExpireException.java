package com.gltqe.wladmin.commons.exception;

/**
 * token异常
 *
 * @author gltqe
 * @date 2022/7/3 0:37
 **/
public class TokenExpireException extends RuntimeException {
    public TokenExpireException() {
        super();
    }

    public TokenExpireException(String message) {
        super(message);
    }

    public TokenExpireException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExpireException(Throwable cause) {
        super(cause);
    }

    protected TokenExpireException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
