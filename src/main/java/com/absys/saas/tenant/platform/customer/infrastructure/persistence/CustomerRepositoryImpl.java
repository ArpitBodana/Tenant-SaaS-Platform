package com.absys.saas.tenant.platform.customer.infrastructure.persistence;

import com.absys.saas.tenant.platform.customer.domain.model.Customer;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerEmail;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerId;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerName;
import com.absys.saas.tenant.platform.customer.domain.model.CustomerStatus;
import com.absys.saas.tenant.platform.customer.domain.repository.CustomerRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final SpringDataCustomerRepository repository;

    public CustomerRepositoryImpl(SpringDataCustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer save(Customer customer) {

        CustomerJpaEntity entity = new CustomerJpaEntity(customer.id().value(), customer.tenantId(), customer.name().value(), customer.email().value(), customer.phone(), customer.status().name());

        CustomerJpaEntity saved = repository.save(entity);

        return toDomain(saved);
    }

    @Override
    public Optional<Customer> findByIdAndTenantId(CustomerId customerId, UUID tenantId) {

        return repository.findByIdAndTenantId(customerId.value(), tenantId).map(this::toDomain);
    }

    @Override
    public List<Customer> findAllByTenantId(UUID tenantId) {

        return repository.findAllByTenantId(tenantId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByEmailAndTenantId(String email, UUID tenantId) {

        return repository.existsByEmailAndTenantId(email.trim().toLowerCase(), tenantId);
    }

    @Override
    public boolean existsByEmailAndTenantIdAndIdNot(String email, UUID tenantId, CustomerId customerId) {

        return repository.existsByEmailAndTenantIdAndIdNot(email.trim().toLowerCase(), tenantId, customerId.value());
    }

    private Customer toDomain(CustomerJpaEntity entity) {

        return Customer.restore(
                CustomerId.of(entity.getId()),
                entity.getTenantId(),
                new CustomerName(entity.getName()),
                new CustomerEmail(entity.getEmail()),
                entity.getPhone(),
                CustomerStatus.valueOf(entity.getStatus())
        );
    }
}