package com.absys.saas.tenant.platform.outbox.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxEventRepository extends JpaRepository<OutboxEventJpaEntity, UUID> {

    @Query("""
            select e
            from OutboxEventJpaEntity e
            where e.processedAt is null
            order by e.createdAt
            """)
    List<OutboxEventJpaEntity> findUnprocessed();
}