package com.absys.saas.tenant.platform.subscription.application.exception;

public class InactiveSubscriptionException extends RuntimeException {

    public InactiveSubscriptionException(String message) {
        super(message);
    }
}