package com.absys.saas.tenant.platform.tenant.application.service;

import com.absys.saas.tenant.platform.tenant.application.command.*;
import com.absys.saas.tenant.platform.tenant.domain.model.*;
import com.absys.saas.tenant.platform.tenant.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TenantCommandService {

    private final TenantRepository tenantRepository;

    public TenantCommandService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public UUID create(CreateTenantCommand command) {

        if (tenantRepository.existsByName(command.name())) {
            throw new IllegalStateException("Tenant already exists");
        }

        Tenant tenant = Tenant.create(TenantId.generate(), new TenantName(command.name()));

        tenantRepository.save(tenant);

        return tenant.id().value();
    }

    @Transactional
    public void activate(ActivateTenantCommand command) {

        Tenant tenant = getTenant(command.tenantId());

        tenant.activate();

        tenantRepository.save(tenant);
    }

    @Transactional
    public void suspend(SuspendTenantCommand command) {

        Tenant tenant = getTenant(command.tenantId());

        tenant.suspend();

        tenantRepository.save(tenant);
    }

    private Tenant getTenant(UUID tenantId) {

        return tenantRepository.findById(TenantId.of(tenantId)).orElseThrow(() -> new IllegalArgumentException("Tenant not found"));
    }
}