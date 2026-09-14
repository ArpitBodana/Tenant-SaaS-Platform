package com.absys.saas.tenant.platform.tenant.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataTenantRepository extends JpaRepository<TenantJpaEntity, UUID> {

    boolean existsByName(String name);
}