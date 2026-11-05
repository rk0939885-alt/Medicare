package com.medconnect.tenant;

import java.util.UUID;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class TenantProvider {
    public UUID requireTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken jwtAuthentication)) {
            throw new InsufficientAuthenticationException("A verified tenant identity is required");
        }
        String tenantClaim = jwtAuthentication.getToken().getClaimAsString("tenant_id");
        try {
            if (tenantClaim == null || tenantClaim.isBlank()) {
                throw new IllegalArgumentException("Missing tenant_id claim");
            }
            return UUID.fromString(tenantClaim);
        } catch (IllegalArgumentException exception) {
            throw new InsufficientAuthenticationException("Token has no valid tenant_id claim", exception);
        }
    }
}