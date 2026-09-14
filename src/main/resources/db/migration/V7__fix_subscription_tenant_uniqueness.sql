ALTER TABLE subscriptions
DROP CONSTRAINT IF EXISTS uk_subscriptions_tenant;

CREATE UNIQUE INDEX uk_subscriptions_active_tenant
    ON subscriptions(tenant_id)
    WHERE status = 'ACTIVE';