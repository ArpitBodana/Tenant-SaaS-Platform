CREATE TABLE products (
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    name VARCHAR(150) NOT NULL,

    sku VARCHAR(100) NOT NULL,

    price NUMERIC(19, 2) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_products_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_products_tenant_sku
        UNIQUE (tenant_id, sku),

    CONSTRAINT chk_products_price_non_negative
        CHECK (price >= 0)
);

CREATE INDEX idx_products_tenant_id
    ON products(tenant_id);

CREATE INDEX idx_products_tenant_status
    ON products(tenant_id, status);

CREATE INDEX idx_products_tenant_name
    ON products(tenant_id, name);