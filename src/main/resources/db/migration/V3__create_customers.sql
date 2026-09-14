CREATE TABLE customers (
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    name VARCHAR(150) NOT NULL,

    email VARCHAR(255) NOT NULL,

    phone VARCHAR(30),

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_customers_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_customers_tenant_email
        UNIQUE (tenant_id, email)
);

CREATE INDEX idx_customers_tenant_id
    ON customers(tenant_id);

CREATE INDEX idx_customers_tenant_status
    ON customers(tenant_id, status);