package com.absys.saas.tenant.platform.identity.application.authorization;

import com.absys.saas.tenant.platform.identity.domain.model.UserRole;
import com.absys.saas.tenant.platform.identity.infrastructure.security.CurrentUser;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {

    public boolean isSuperAdmin(CurrentUser user) {

        return user.role() == UserRole.SUPER_ADMIN;
    }

    public boolean isTenantAdmin(CurrentUser user) {

        return user.role() == UserRole.TENANT_ADMIN;
    }

    public boolean belongsToTenant(CurrentUser user, UUID tenantId) {

        if (user.role() == UserRole.SUPER_ADMIN) {
            return true;
        }

        return tenantId != null && tenantId.equals(user.tenantId());
    }

    public void requireTenantAccess(CurrentUser user, UUID tenantId) {

        if (!belongsToTenant(user, tenantId)) {

            throw new SecurityException("User does not have access to this tenant");
        }
    }
}