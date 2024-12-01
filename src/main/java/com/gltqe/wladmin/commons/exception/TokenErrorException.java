package com.gltqe.wladmin.commons.exception;

/**
 * token异常
 *
 * @author gltqe
 * @date 2022/7/3 0:37
 **/
public class TokenErrorException extends RuntimeException {
    public TokenErrorException() {
        super();
    }

    public TokenErrorException(String message) {
        super(message);
    }

    public TokenErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenErrorException(Throwable cause) {
        super(cause);
    }

    protected TokenErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
