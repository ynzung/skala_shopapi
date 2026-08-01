package com.sk.skala.shopapi.exception;

public class RewardAlreadyReceivedException extends RuntimeException {
    public RewardAlreadyReceivedException(String message) {
        super(message);
    }
}
