package com.absys.saas.tenant.platform.audit.application.service;

import com.absys.saas.tenant.platform.audit.application.command.RecordAuditCommand;
import com.absys.saas.tenant.platform.audit.domain.model.AuditEvent;
import com.absys.saas.tenant.platform.audit.domain.model.AuditEventId;
import com.absys.saas.tenant.platform.audit.domain.repository.AuditEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditCommandService {

    private final AuditEventRepository auditEventRepository;

    public AuditCommandService(
            AuditEventRepository auditEventRepository) {

        this.auditEventRepository = auditEventRepository;
    }

    @Transactional
    public void record(RecordAuditCommand command) {

        AuditEvent event = AuditEvent.create(
                AuditEventId.generate(),
                command.tenantId(),
                command.actorId(),
                command.actorType(),
                command.action(),
                command.aggregateType(),
                command.aggregateId(),
                command.description()
        );

        auditEventRepository.save(event);
    }
}