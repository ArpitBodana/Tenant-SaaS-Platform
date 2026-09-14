package com.absys.saas.tenant.platform.identity.application.authentication;

import com.absys.saas.tenant.platform.identity.domain.model.User;
import com.absys.saas.tenant.platform.identity.domain.model.UserStatus;
import com.absys.saas.tenant.platform.identity.domain.repository.UserRepository;
import com.absys.saas.tenant.platform.identity.infrastructure.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse login(LoginCommand command) {

        User user = userRepository.findByEmail(command.email()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (user.status() == UserStatus.INACTIVE) {
            throw new IllegalStateException("User account is inactive");
        }

        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token, "Bearer", user.id().value(), user.tenantId(), user.role().name());
    }
}