from datetime import datetime
from uuid import UUID

from pydantic import BaseModel, ConfigDict, Field


class EventMetadata(BaseModel):
    model_config = ConfigDict(
        populate_by_name=True
    )

    event_id: UUID = Field(alias="eventId")
    correlation_id: UUID | None = Field(alias="correlationId")
    occurred_at: datetime = Field(alias="occurredAt")
    event_type: str = Field(alias="eventType")
    event_version: int = Field(alias="eventVersion")