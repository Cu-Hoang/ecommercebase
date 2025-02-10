package com.project.ecommercebase.configuration;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class AuthId {
    public UUID getId() {
        JwtAuthenticationToken authToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(authToken.getName());
    }
}
