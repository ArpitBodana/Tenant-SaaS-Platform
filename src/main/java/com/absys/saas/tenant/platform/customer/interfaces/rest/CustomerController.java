package com.absys.saas.tenant.platform.customer.interfaces.rest;

import com.absys.saas.tenant.platform.customer.application.command.ActivateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.command.CreateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.command.DeactivateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.command.UpdateCustomerCommand;
import com.absys.saas.tenant.platform.customer.application.dto.CustomerResponse;
import com.absys.saas.tenant.platform.customer.application.query.GetCustomerQuery;
import com.absys.saas.tenant.platform.customer.application.query.GetCustomersQuery;
import com.absys.saas.tenant.platform.customer.application.service.CustomerCommandService;
import com.absys.saas.tenant.platform.customer.application.service.CustomerQueryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerCommandService commandService;
    private final CustomerQueryService queryService;

    public CustomerController(CustomerCommandService commandService, CustomerQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {

        CustomerResponse response = commandService.create(new CreateCustomerCommand(request.name(), request.email(), request.phone()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerId}")
    public CustomerResponse get(@PathVariable UUID customerId) {

        return queryService.get(new GetCustomerQuery(customerId));
    }

    @GetMapping
    public List<CustomerResponse> getAll() {

        return queryService.getAll(new GetCustomersQuery());
    }

    @PutMapping("/{customerId}")
    public CustomerResponse update(@PathVariable UUID customerId, @Valid @RequestBody UpdateCustomerRequest request) {

        return commandService.update(new UpdateCustomerCommand(customerId, request.name(), request.email(), request.phone()));
    }

    @PatchMapping("/{customerId}/activate")
    public CustomerResponse activate(@PathVariable UUID customerId) {

        return commandService.activate(new ActivateCustomerCommand(customerId));
    }

    @PatchMapping("/{customerId}/deactivate")
    public CustomerResponse deactivate(@PathVariable UUID customerId) {

        return commandService.deactivate(new DeactivateCustomerCommand(customerId));
    }

    public record CreateCustomerRequest(

            @NotBlank @Size(max = 150) String name,

            @NotBlank @Email @Size(max = 255) String email,

            @Size(max = 30) String phone

    ) {
    }

    public record UpdateCustomerRequest(

            @NotBlank @Size(max = 150) String name,

            @NotBlank @Email @Size(max = 255) String email,

            @Size(max = 30) String phone

    ) {
    }
}