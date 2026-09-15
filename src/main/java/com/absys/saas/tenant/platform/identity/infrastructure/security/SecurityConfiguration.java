package com.absys.saas.tenant.platform.identity.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/auth/**", "/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers("/api/platform/**").hasRole("SUPER_ADMIN")

                        // Customers
                        .requestMatchers("/api/customers/**").hasAnyRole("TENANT_ADMIN", "USER")

                        // Products - read
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("TENANT_ADMIN", "USER")

                        // Products - write
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("TENANT_ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("TENANT_ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("TENANT_ADMIN")

                        // Orders - read
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("TENANT_ADMIN", "USER")

                        // Orders - create
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("TENANT_ADMIN", "USER")

                        // Add order item
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/items").hasAnyRole("TENANT_ADMIN", "USER")

                        // Remove order item
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/*/items/*").hasAnyRole("TENANT_ADMIN", "USER")

                        // Confirm / cancel
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/confirm", "/api/orders/*/cancel").hasAnyRole("TENANT_ADMIN", "USER")

                        .requestMatchers(HttpMethod.GET, "/api/subscriptions", "/api/subscriptions/**").hasAnyRole("TENANT_ADMIN", "USER")

                        .requestMatchers(HttpMethod.POST, "/api/subscriptions").hasRole("TENANT_ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/subscriptions/**").hasRole("TENANT_ADMIN").anyRequest().authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/notifications/**").hasAnyRole("TENANT_ADMIN", "USER")

                        .requestMatchers(HttpMethod.POST, "/api/notifications").hasRole("TENANT_ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/notifications/**").hasRole("TENANT_ADMIN"))

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}