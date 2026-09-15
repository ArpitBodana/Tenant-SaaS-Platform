ALTER TABLE outbox_events
ADD COLUMN status VARCHAR(20);

UPDATE outbox_events
SET status =
    CASE
        WHEN processed_at IS NOT NULL THEN 'PROCESSED'
        ELSE 'PENDING'
    END;

ALTER TABLE outbox_events
ALTER COLUMN status SET NOT NULL;

CREATE INDEX idx_outbox_events_status
ON outbox_events (status, created_at);