package com.absys.saas.tenant.platform.customer.application.service;

import com.absys.saas.tenant.platform.customer.application.command.ActivateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.command.CreateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.command.DeactivateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.command.UpdateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.dto.CustomerResponse;
import com.absys.saas.tenant.platform.customer.domain.model.Customer;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerEmail;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerId;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerName;
import com.absys.saas.tenant.platform.customer.domain.repository.CustomerRepository;
import com.absys.saas.tenant.platform.identity.infrastructure.security.TenantContext;

import com.absys.saas.tenant.platform.shared.domain.exception.NotFoundException;
import com.absys.saas.tenant.platform.subscription.application.security.RequiresActiveSubscription;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class CustomerCommandService {

    private final CustomerRepository customerRepository;

    public CustomerCommandService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequiresActiveSubscription
    public CustomerResponse create(CreateCustomerCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        CustomerEmail email = new CustomerEmail(command.email());

        if (customerRepository.existsByEmailAndTenantId(email.value(), tenantId)) {
            throw new IllegalArgumentException("Customer email already exists");
        }

        Customer customer = Customer.create(CustomerId.generate(), tenantId, new CustomerName(command.name()), email, command.phone());

        Customer saved = customerRepository.save(customer);

        return toResponse(saved);
    }

    @RequiresActiveSubscription
    public CustomerResponse update(UpdateCustomerCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        CustomerId customerId = CustomerId.of(command.customerId());

        Customer customer = customerRepository.findByIdAndTenantId(customerId, tenantId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        CustomerEmail email = new CustomerEmail(command.email());

        if (customerRepository.existsByEmailAndTenantIdAndIdNot(email.value(), tenantId, customerId)) {

            throw new IllegalArgumentException("Customer email already exists");
        }

        customer.update(new CustomerName(command.name()), email, command.phone());

        Customer saved = customerRepository.save(customer);

        return toResponse(saved);
    }

    @RequiresActiveSubscription
    public CustomerResponse activate(ActivateCustomerCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(CustomerId.of(command.customerId()), tenantId).orElseThrow(() -> new NotFoundException("Customer not found"));

        customer.activate();

        return toResponse(customerRepository.save(customer));
    }

    @RequiresActiveSubscription
    public CustomerResponse deactivate(DeactivateCustomerCommand command) {

        UUID tenantId = TenantContext.requireTenantId();

        Customer customer = customerRepository.findByIdAndTenantId(CustomerId.of(command.customerId()), tenantId).orElseThrow(() -> new NotFoundException("Customer not found"));

        customer.deactivate();

        return toResponse(customerRepository.save(customer));
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(customer.id().value(), customer.name().value(), customer.email().value(), customer.phone(), customer.status());
    }
}