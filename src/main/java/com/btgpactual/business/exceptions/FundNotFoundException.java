package com.btgpactual.business.exceptions;

public class FundNotFoundException extends RuntimeException {
    public FundNotFoundException(String message) {
        super(message);
    }
}
