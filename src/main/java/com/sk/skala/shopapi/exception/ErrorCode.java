package com.sk.skala.shopapi.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INSUFFICIENT_FUNDS(HttpStatus.BAD_REQUEST),
    PARAMETER_ERROR(HttpStatus.BAD_REQUEST),
    DUPLICATE_CUSTOMER_ID(HttpStatus.CONFLICT),
    REWARD_ALREADY_RECEIVED(HttpStatus.CONFLICT),
    FAVORITE_ALREADY_EXISTS(HttpStatus.CONFLICT),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
