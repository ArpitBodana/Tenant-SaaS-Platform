package com.absys.saas.tenant.platform.customer.domain.repository;

import com.absys.saas.tenant.platform.customer.domain.model.Customer;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findByIdAndTenantId(CustomerId customerId, UUID tenantId);

    List<Customer> findAllByTenantId(UUID tenantId);

    boolean existsByEmailAndTenantId(String email, UUID tenantId);

    boolean existsByEmailAndTenantIdAndIdNot(String email, UUID tenantId, CustomerId customerId);
}