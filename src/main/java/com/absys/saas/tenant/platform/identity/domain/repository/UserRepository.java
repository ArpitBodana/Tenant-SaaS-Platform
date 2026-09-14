package com.absys.saas.tenant.platform.identity.domain.repository;

import com.absys.saas.tenant.platform.identity.domain.model.User;
import com.absys.saas.tenant.platform.identity.domain.model.UserId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndTenantId(String email, UUID tenantId);

    List<User> findByTenantId(UUID tenantId);

    boolean existsByEmail(String email);

    boolean existsByEmailAndTenantId(String email, UUID tenantId);
}