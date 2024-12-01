package com.gltqe.wladmin.commons.exception;

/**
 * 权限异常
 *
 * @author gltqe
 * @date 2022/7/3 0:37
 **/
public class PermissionException extends RuntimeException {
    public PermissionException() {
        super();
    }

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionException(Throwable cause) {
        super(cause);
    }

    protected PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
