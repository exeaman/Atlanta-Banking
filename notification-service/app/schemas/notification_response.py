from datetime import datetime
from uuid import UUID

from pydantic import BaseModel

from app.enums.notification_channel import NotificationChannel
from app.enums.notification_status import NotificationStatus


class NotificationResponse(BaseModel):
    id: UUID

    recipient: str

    channel: NotificationChannel

    subject: str | None = None

    body: str

    status: NotificationStatus

    failure_reason: str | None = None

    created_at: datetime

    sent_at: datetime | None = None