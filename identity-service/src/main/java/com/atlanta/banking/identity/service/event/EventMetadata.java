package com.atlanta.banking.identity.service.event;


import java.time.Instant;
import java.util.UUID;

public record EventMetadata(
        UUID eventId,
        UUID correlationId,
        Instant occurredAt,
        String eventType,
        int eventVersion
) {
}