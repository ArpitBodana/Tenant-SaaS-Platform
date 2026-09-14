CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    plan VARCHAR(30) NOT NULL,

    billing_cycle VARCHAR(30) NOT NULL,

    status VARCHAR(30) NOT NULL,

    start_date DATE NOT NULL,

    end_date DATE NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_subscriptions_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT uk_subscriptions_tenant
        UNIQUE (tenant_id),

    CONSTRAINT chk_subscription_dates
        CHECK (end_date >= start_date)
);

CREATE INDEX idx_subscriptions_tenant_status
    ON subscriptions(tenant_id, status);

CREATE INDEX idx_subscriptions_status
    ON subscriptions(status);

CREATE INDEX idx_subscriptions_end_date
    ON subscriptions(end_date);