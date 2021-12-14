package com.terahash.arbitrage.authentication.exception;

public class TokenValidationException extends AuthenticationException {

    public TokenValidationException(String message) {
        super(message);
    }

    public TokenValidationException() {
    }
}
