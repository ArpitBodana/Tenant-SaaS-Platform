package com.absys.saas.tenant.platform.identity.infrastructure.security;

import com.absys.saas.tenant.platform.identity.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtService(@Value("${security.jwt.secret}") String secret, @Value("${security.jwt.expiration-ms}") long expirationMillis) {

        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        this.expirationMillis = expirationMillis;
    }

    public String generateToken(User user) {

        Instant now = Instant.now();

        return Jwts.builder().subject(user.id().value().toString()).claim("tenantId", user.tenantId() != null ? user.tenantId().toString() : null).claim("role", user.role().name()).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expirationMillis))).signWith(secretKey).compact();
    }

    public Claims extractClaims(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public UUID extractUserId(String token) {

        return UUID.fromString(extractClaims(token).getSubject());
    }

    public UUID extractTenantId(String token) {

        Object tenantId = extractClaims(token).get("tenantId");

        if (tenantId == null) {
            return null;
        }

        return UUID.fromString(tenantId.toString());
    }

    public String extractRole(String token) {

        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {

        try {
            Claims claims = extractClaims(token);

            return claims.getExpiration().after(new Date());

        } catch (Exception e) {
            return false;
        }
    }
}