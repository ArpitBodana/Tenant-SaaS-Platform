package com.absys.saas.tenant.platform.tenant.interfaces.rest;

import com.absys.saas.tenant.platform.tenant.application.command.*;
import com.absys.saas.tenant.platform.tenant.application.dto.TenantResponse;
import com.absys.saas.tenant.platform.tenant.application.query.GetTenantQuery;
import com.absys.saas.tenant.platform.tenant.application.service.TenantCommandService;
import com.absys.saas.tenant.platform.tenant.application.service.TenantQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/platform/tenants")
public class TenantController {

    private final TenantCommandService commandService;
    private final TenantQueryService queryService;

    public TenantController(TenantCommandService commandService, TenantQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<UUID> create(@Valid @RequestBody CreateTenantRequest request) {

        UUID tenantId = commandService.create(new CreateTenantCommand(request.name()));

        return ResponseEntity.ok(tenantId);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<TenantResponse> get(@PathVariable UUID tenantId) {

        return ResponseEntity.ok(queryService.get(new GetTenantQuery(tenantId)));
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> getAll() {

        return ResponseEntity.ok(queryService.getAll());
    }

    @PatchMapping("/{tenantId}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID tenantId) {

        commandService.activate(new ActivateTenantCommand(tenantId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tenantId}/suspend")
    public ResponseEntity<Void> suspend(@PathVariable UUID tenantId) {

        commandService.suspend(new SuspendTenantCommand(tenantId));

        return ResponseEntity.noContent().build();
    }

    public record CreateTenantRequest(

            @NotBlank(message = "Tenant name is required") @Size(max = 150) String name

    ) {
    }
}