package com.github.datnm23.acountservice.exception;

public class ExpiredEmailActivationUrlException extends Exception {
    public ExpiredEmailActivationUrlException(String message) {
        super(message);
    }
}
