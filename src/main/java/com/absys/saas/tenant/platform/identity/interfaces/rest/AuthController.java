package com.absys.saas.tenant.platform.identity.interfaces.rest;

import com.absys.saas.tenant.platform.identity.application.authentication.AuthenticationResponse;
import com.absys.saas.tenant.platform.identity.application.authentication.AuthenticationService;
import com.absys.saas.tenant.platform.identity.application.authentication.LoginCommand;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {

        AuthenticationResponse response = authenticationService.login(new LoginCommand(request.email(), request.password()));

        return ResponseEntity.ok(response);
    }

    public record LoginRequest(

            @NotBlank @Email String email,

            @NotBlank String password) {
    }
}