package com.absys.saas.tenant.platform.notification.interfaces.rest;

import com.absys.saas.tenant.platform.notification.application.command.CreateNotificationCommand;
import com.absys.saas.tenant.platform.notification.application.command.MarkNotificationFailedCommand;
import com.absys.saas.tenant.platform.notification.application.command.MarkNotificationSentCommand;
import com.absys.saas.tenant.platform.notification.application.dto.NotificationResponse;
import com.absys.saas.tenant.platform.notification.application.query.GetNotificationQuery;
import com.absys.saas.tenant.platform.notification.application.query.GetNotificationsQuery;
import com.absys.saas.tenant.platform.notification.application.service.NotificationCommandService;
import com.absys.saas.tenant.platform.notification.application.service.NotificationQueryService;
import com.absys.saas.tenant.platform.notification.domain.model.NotificationChannel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationCommandService commandService;
    private final NotificationQueryService queryService;

    public NotificationController(NotificationCommandService commandService, NotificationQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public NotificationResponse create(@Valid @RequestBody CreateNotificationRequest request) {

        return commandService.create(new CreateNotificationCommand(request.channel(), request.recipient(), request.subject(), request.message()));
    }

    @GetMapping
    public List<NotificationResponse> getAll() {

        return queryService.getAll(new GetNotificationsQuery());
    }

    @GetMapping("/{notificationId}")
    public NotificationResponse get(@PathVariable UUID notificationId) {

        return queryService.get(new GetNotificationQuery(notificationId));
    }

    @PatchMapping("/{notificationId}/sent")
    public NotificationResponse markSent(@PathVariable UUID notificationId) {

        return commandService.markSent(new MarkNotificationSentCommand(notificationId));
    }

    @PatchMapping("/{notificationId}/failed")
    public NotificationResponse markFailed(@PathVariable UUID notificationId, @Valid @RequestBody MarkFailedRequest request) {

        return commandService.markFailed(new MarkNotificationFailedCommand(notificationId, request.reason()));
    }

    public record CreateNotificationRequest(

            @NotNull NotificationChannel channel,

            @NotBlank @Size(max = 255) String recipient,

            @Size(max = 255) String subject,

            @NotBlank @Size(max = 5000) String message) {
    }

    public record MarkFailedRequest(

            @NotBlank @Size(max = 1000) String reason) {
    }
}