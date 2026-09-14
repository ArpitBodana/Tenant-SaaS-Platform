package com.absys.saas.tenant.platform.tenant.infrastructure.persistence;

import com.absys.saas.tenant.platform.tenant.domain.model.*;
import com.absys.saas.tenant.platform.tenant.domain.repository.TenantRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TenantRepositoryImpl implements TenantRepository {

    private final SpringDataTenantRepository repository;

    public TenantRepositoryImpl(SpringDataTenantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Tenant save(Tenant tenant) {

        TenantJpaEntity entity = new TenantJpaEntity(tenant.id().value(), tenant.name().value(), tenant.status().name());

        repository.save(entity);

        return tenant;
    }

    @Override
    public Optional<Tenant> findById(TenantId tenantId) {

        return repository.findById(tenantId.value()).map(this::toDomain);
    }

    @Override
    public List<Tenant> findAll() {

        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByName(String name) {

        return repository.existsByName(name);
    }

    private Tenant toDomain(TenantJpaEntity entity) {

        Tenant tenant = Tenant.create(TenantId.of(entity.getId()), new TenantName(entity.getName()));

        if ("SUSPENDED".equals(entity.getStatus())) {
            tenant.suspend();
        }

        return tenant;
    }
}