CREATE TABLE notifications (
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    channel VARCHAR(30) NOT NULL,

    recipient VARCHAR(255) NOT NULL,

    subject VARCHAR(255),

    message VARCHAR(5000) NOT NULL,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    sent_at TIMESTAMP WITH TIME ZONE,

    failure_reason VARCHAR(1000),

    CONSTRAINT fk_notifications_tenant
        FOREIGN KEY (tenant_id)
        REFERENCES tenants(id),

    CONSTRAINT chk_notifications_status
        CHECK (
            status IN (
                'PENDING',
                'SENT',
                'FAILED'
            )
        ),

    CONSTRAINT chk_notifications_channel
        CHECK (
            channel IN (
                'EMAIL',
                'SMS',
                'IN_APP'
            )
        )
);

CREATE INDEX idx_notifications_tenant
    ON notifications(tenant_id);

CREATE INDEX idx_notifications_tenant_status
    ON notifications(tenant_id, status);

CREATE INDEX idx_notifications_tenant_created
    ON notifications(tenant_id, created_at DESC);