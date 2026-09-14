package com.absys.saas.tenant.platform.tenant.application.service;

import com.absys.saas.tenant.platform.tenant.application.dto.TenantResponse;
import com.absys.saas.tenant.platform.tenant.application.query.GetTenantQuery;
import com.absys.saas.tenant.platform.tenant.domain.model.Tenant;
import com.absys.saas.tenant.platform.tenant.domain.model.TenantId;
import com.absys.saas.tenant.platform.tenant.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TenantQueryService {

    private final TenantRepository tenantRepository;

    public TenantQueryService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public TenantResponse get(GetTenantQuery query) {

        Tenant tenant = tenantRepository.findById(TenantId.of(query.tenantId())).orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        return toResponse(tenant);
    }

    public List<TenantResponse> getAll() {

        return tenantRepository.findAll().stream().map(this::toResponse).toList();
    }

    private TenantResponse toResponse(Tenant tenant) {

        return new TenantResponse(tenant.id().value(), tenant.name().value(), tenant.status().name());
    }
}