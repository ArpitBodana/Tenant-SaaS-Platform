package com.absys.saas.tenant.platform.identity.application.service;

import com.absys.saas.tenant.platform.identity.application.command.CreateUserCommand;
import com.absys.saas.tenant.platform.identity.application.command.ActivateUserCommand;
import com.absys.saas.tenant.platform.identity.application.command.DeactivateUserCommand;

import com.absys.saas.tenant.platform.identity.domain.model.*;
import com.absys.saas.tenant.platform.identity.domain.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(CreateUserCommand command) {

        String email = command.email().trim().toLowerCase();

        if (command.role() == UserRole.SUPER_ADMIN) {

            if (command.tenantId() != null) {
                throw new IllegalArgumentException("SUPER_ADMIN cannot have tenantId");
            }

            if (userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("Email already exists");
            }

        } else {

            if (command.tenantId() == null) {
                throw new IllegalArgumentException("Tenant ID is required");
            }

            if (userRepository.existsByEmailAndTenantId(email, command.tenantId())) {
                throw new IllegalArgumentException("Email already exists in tenant");
            }
        }

        String passwordHash = passwordEncoder.encode(command.password());

        User user = User.create(UserId.generate(), command.tenantId(), new UserEmail(email), passwordHash, command.role());

        return userRepository.save(user);
    }

    public void activate(ActivateUserCommand command) {

        User user = userRepository.findById(UserId.of(command.userId())).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.activate();

        userRepository.save(user);
    }

    public void deactivate(DeactivateUserCommand command) {

        User user = userRepository.findById(UserId.of(command.userId())).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.deactivate();

        userRepository.save(user);
    }
}