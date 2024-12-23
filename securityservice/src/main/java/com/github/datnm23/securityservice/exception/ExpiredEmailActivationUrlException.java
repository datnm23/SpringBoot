package com.github.datnm23.securityservice.exception;

public class ExpiredEmailActivationUrlException extends Exception {
    public ExpiredEmailActivationUrlException(String message) {
        super(message);
    }
}
