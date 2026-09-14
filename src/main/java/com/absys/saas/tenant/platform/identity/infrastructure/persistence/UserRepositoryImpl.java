package com.absys.saas.tenant.platform.identity.infrastructure.persistence;

import com.absys.saas.tenant.platform.identity.domain.model.*;
import com.absys.saas.tenant.platform.identity.domain.repository.UserRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SpringDataUserRepository repository;

    public UserRepositoryImpl(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {

        UserJpaEntity entity = new UserJpaEntity(user.id().value(), user.tenantId(), user.email().value(), user.passwordHash(), user.role().name(), user.status().name());

        UserJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId userId) {

        return repository.findById(userId.value()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return repository.findByEmail(email.trim().toLowerCase()).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmailAndTenantId(String email, UUID tenantId) {

        return repository.findByEmailAndTenantId(email.trim().toLowerCase(), tenantId).map(this::toDomain);
    }

    @Override
    public List<User> findByTenantId(UUID tenantId) {

        return repository.findByTenantId(tenantId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByEmail(String email) {

        return repository.existsByEmail(email.trim().toLowerCase());
    }

    @Override
    public boolean existsByEmailAndTenantId(String email, UUID tenantId) {

        return repository.existsByEmailAndTenantId(email.trim().toLowerCase(), tenantId);
    }

    private User toDomain(
            UserJpaEntity entity
    ) {

        return User.restore(
                UserId.of(entity.getId()),
                entity.getTenantId(),
                new UserEmail(entity.getEmail()),
                entity.getPasswordHash(),
                UserRole.valueOf(entity.getRole()),
                UserStatus.valueOf(entity.getStatus())
        );
    }
}