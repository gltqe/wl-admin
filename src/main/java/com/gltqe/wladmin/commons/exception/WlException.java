package com.gltqe.wladmin.commons.exception;

/**
 * 其他服务异常
 *
 * @author gltqe
 * @date 2022/7/3 0:37
 **/
public class WlException extends RuntimeException {
    public WlException() {
        super();
    }

    public WlException(String message) {
        super(message);
    }

    public WlException(String message, Throwable cause) {
        super(message, cause);
    }

    public WlException(Throwable cause) {
        super(cause);
    }

    protected WlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
