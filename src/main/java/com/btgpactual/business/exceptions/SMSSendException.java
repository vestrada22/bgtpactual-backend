package com.btgpactual.business.exceptions;

public class SMSSendException extends RuntimeException {
    public SMSSendException(String message) {
        super(message);
    }

    public SMSSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
