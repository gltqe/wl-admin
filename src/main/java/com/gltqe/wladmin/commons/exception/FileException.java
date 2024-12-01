package com.gltqe.wladmin.commons.exception;

/**
 * 文件异常
 *
 * @author gltqe
 * @date 2022/7/3 0:36
 **/
public class FileException extends RuntimeException {
    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    protected FileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
