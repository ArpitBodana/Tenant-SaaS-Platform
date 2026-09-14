package com.absys.saas.tenant.platform.tenant.domain.repository;

import com.absys.saas.tenant.platform.tenant.domain.model.Tenant;
import com.absys.saas.tenant.platform.tenant.domain.model.TenantId;

import java.util.List;
import java.util.Optional;

public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(TenantId tenantId);

    List<Tenant> findAll();

    boolean existsByName(String name);
}