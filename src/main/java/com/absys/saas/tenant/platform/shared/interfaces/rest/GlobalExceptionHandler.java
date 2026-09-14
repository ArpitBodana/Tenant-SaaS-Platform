package com.absys.saas.tenant.platform.shared.interfaces.rest;

import com.absys.saas.tenant.platform.subscription.application.exception.InactiveSubscriptionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InactiveSubscriptionException.class)
    public ResponseEntity<ErrorResponse> handleInactiveSubscription(InactiveSubscriptionException exception) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("SUBSCRIPTION_INACTIVE", exception.getMessage()));
    }

    public record ErrorResponse(String code, String message) {
    }
}