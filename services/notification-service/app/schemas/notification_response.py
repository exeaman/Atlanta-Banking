from datetime import datetime
from uuid import UUID

from pydantic import BaseModel, ConfigDict

from app.enums.notification_channel import NotificationChannel
from app.enums.notification_status import NotificationStatus


class NotificationResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: UUID

    event_id: UUID
    correlation_id: UUID | None
    system_id: UUID

    recipient: str

    channel: NotificationChannel

    subject: str
    body: str

    status: NotificationStatus

    created_at: datetime