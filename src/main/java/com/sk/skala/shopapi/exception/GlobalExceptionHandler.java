package com.sk.skala.shopapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(
            AuthenticationException exception
    ) {
        return response(
                ErrorCode.AUTHENTICATION_FAILED,
                "아이디 또는 비밀번호가 일치하지 않습니다."
        );
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientFunds(
            InsufficientFundsException exception
    ) {
        return response(ErrorCode.INSUFFICIENT_FUNDS, exception.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFound(
            DataNotFoundException exception
    ) {
        return response(ErrorCode.DATA_NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateCustomer(
            DuplicateCustomerException exception
    ) {
        return response(ErrorCode.DUPLICATE_CUSTOMER_ID, exception.getMessage());
    }

    @ExceptionHandler(RewardAlreadyReceivedException.class)
    public ResponseEntity<ErrorResponse> handleRewardAlreadyReceived(
            RewardAlreadyReceivedException exception
    ) {
        return response(ErrorCode.REWARD_ALREADY_RECEIVED, exception.getMessage());
    }

    @ExceptionHandler(ParameterException.class)
    public ResponseEntity<ErrorResponse> handleParameter(
            ParameterException exception
    ) {
        return response(ErrorCode.PARAMETER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("요청 값이 올바르지 않습니다.");
        return handleParameter(new ParameterException(message));
    }

    private ResponseEntity<ErrorResponse> response(
            ErrorCode errorCode,
            String message
    ) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode.name(), message));
    }
}
