package com.absys.saas.tenant.platform.identity.application.authentication;

public record LoginCommand(String email, String password) {
}