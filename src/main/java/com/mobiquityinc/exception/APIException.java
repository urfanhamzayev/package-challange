package com.mobiquityinc.exception;

public class APIException extends Exception {
    private String line;
    public APIException() {
    }

    public APIException(Throwable cause, String line) {
        super(cause);
        this.line = line;
    }

    public APIException(String message, String line) {
        super(message);
        this.line = line;
    }

    public APIException(String message, Throwable cause, String line) {
        super(message, cause);
        this.line = line;
    }

    public APIException(String message) {
        super(message);
    }

    public APIException(String message, Throwable cause) {
        super(message, cause);
    }

    public APIException(Throwable cause) {
        super(cause);
    }

    public APIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
