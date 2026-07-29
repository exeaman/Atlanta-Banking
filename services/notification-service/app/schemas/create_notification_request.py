from pydantic import BaseModel, EmailStr, Field

from app.enums.notification_channel import NotificationChannel


class CreateNotificationRequest(BaseModel):

    recipient: EmailStr

    channel: NotificationChannel

    subject: str | None = Field(
        default=None,
        max_length=255,
    )

    body: str = Field(
        min_length=1,
        max_length=5000,
    )