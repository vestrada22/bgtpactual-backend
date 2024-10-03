package com.btgpactual.business.exceptions;

public class MinimumAmountNotMetException extends RuntimeException {
    public MinimumAmountNotMetException(String message) {
        super(message);
    }
}
