package com.atlanta.common.events.metadata;

import com.atlanta.common.events.constants.ServiceName;

import java.time.Instant;
import java.util.UUID;

public record EventMetadata(

        UUID eventId,

        UUID correlationId,

        Instant occurredAt,

        ServiceName source,

        String eventName,

        int eventVersion

) {
}