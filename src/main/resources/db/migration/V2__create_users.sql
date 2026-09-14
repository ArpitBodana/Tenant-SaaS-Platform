CREATE TABLE users (
    id UUID PRIMARY KEY,

    tenant_id UUID NULL,

    email VARCHAR(255) NOT NULL,

    password_hash VARCHAR(255) NOT NULL,

    role VARCHAR(50) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_users_tenant_email
        UNIQUE (tenant_id, email)
);

CREATE INDEX idx_users_tenant_id
    ON users(tenant_id);

CREATE INDEX idx_users_status
    ON users(status);

CREATE INDEX idx_users_email
    ON users(email);

CREATE UNIQUE INDEX uk_users_super_admin_email
    ON users(email)
    WHERE tenant_id IS NULL;