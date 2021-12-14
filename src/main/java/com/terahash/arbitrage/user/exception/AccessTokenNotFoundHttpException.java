package com.terahash.arbitrage.user.exception;

import com.terahash.arbitrage.exception.HttpException;
import org.springframework.http.HttpStatus;

public class AccessTokenNotFoundHttpException extends HttpException {

    public AccessTokenNotFoundHttpException(String message, HttpStatus status) {
        super(message, status);
    }
}
