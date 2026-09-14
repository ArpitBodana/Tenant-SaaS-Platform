package com.absys.saas.tenant.platform.customer.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataCustomerRepository extends JpaRepository<CustomerJpaEntity, UUID> {

    Optional<CustomerJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);

    List<CustomerJpaEntity> findAllByTenantId(UUID tenantId);

    boolean existsByEmailAndTenantId(String email, UUID tenantId);

    boolean existsByEmailAndTenantIdAndIdNot(String email, UUID tenantId, UUID id);
}