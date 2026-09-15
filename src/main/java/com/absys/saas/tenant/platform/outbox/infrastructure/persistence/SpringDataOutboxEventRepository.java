package com.absys.saas.tenant.platform.outbox.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataOutboxEventRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {

    @Query(value = """
            SELECT id
            FROM outbox_events
            WHERE status = 'PENDING'
              AND next_attempt_at <= CURRENT_TIMESTAMP
            ORDER BY created_at
            LIMIT :limit
            """, nativeQuery = true)
    List<UUID> findPendingIds(@Param("limit") int limit);

    @Query(value = """
            SELECT *
            FROM outbox_events
            WHERE id = :id
              AND status = 'PENDING'
              AND next_attempt_at <= CURRENT_TIMESTAMP
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<OutboxEventJpaEntity> findPendingByIdForUpdate(@Param("id") UUID id);
}