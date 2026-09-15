ALTER TABLE outbox_events
ADD COLUMN next_attempt_at TIMESTAMP WITH TIME ZONE;

UPDATE outbox_events
SET next_attempt_at = COALESCE(created_at, CURRENT_TIMESTAMP);

ALTER TABLE outbox_events
ALTER COLUMN next_attempt_at SET NOT NULL;

CREATE INDEX idx_outbox_pending_attempt
ON outbox_events(status, next_attempt_at, created_at);