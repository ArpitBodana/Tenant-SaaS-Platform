CREATE TABLE audit_events (
    id UUID PRIMARY KEY,

    tenant_id UUID,

    actor_id UUID,

    actor_type VARCHAR(20) NOT NULL,

    action VARCHAR(50) NOT NULL,

    aggregate_type VARCHAR(100) NOT NULL,

    aggregate_id VARCHAR(100) NOT NULL,

    description TEXT NOT NULL,

    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_audit_events_tenant_occurred
    ON audit_events (tenant_id, occurred_at DESC);

CREATE INDEX idx_audit_events_aggregate
    ON audit_events (aggregate_type, aggregate_id);