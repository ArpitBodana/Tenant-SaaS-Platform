package com.absys.saas.tenant.platform.customer.application.service;

import com.absys.saas.tenant.platform.customer.application.dto.CustomerResponse;
import com.absys.saas.tenant.platform.customer.application.query.GetCustomerQuery;
import com.absys.saas.tenant.platform.customer.application.query.GetCustomersQuery;
import com.absys.saas.tenant.platform.customer.domain.model.Customer;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerId;
import com.absys.saas.tenant.platform.customer.domain.repository.CustomerRepository;
import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public CustomerQueryService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse get(GetCustomerQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(CustomerId.of(query.customerId()), tenantId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        return toResponse(customer);
    }

    public List<CustomerResponse> getAll(GetCustomersQuery query) {

        UUID tenantId = TenantContext.requireTenantId();

        return customerRepository.findAllByTenantId(tenantId).stream().map(this::toResponse).toList();
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(customer.id().value(), customer.name().value(), customer.email().value(), customer.phone(), customer.status());
    }
}