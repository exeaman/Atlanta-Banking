from datetime import datetime
from uuid import UUID, uuid4

from sqlalchemy import DateTime, Enum, String, Text
from sqlalchemy.dialects.postgresql import UUID as PG_UUID
from sqlalchemy.orm import Mapped, mapped_column
from sqlalchemy.sql import func

from app.database.base import Base
from app.enums.notification_channel import NotificationChannel
from app.enums.notification_status import NotificationStatus


class Notification(Base):
    __tablename__ = "notification"
    __table_args__ = {"schema": "notification"}

    id: Mapped[UUID] = mapped_column(
        PG_UUID(as_uuid=True),
        primary_key=True,
        default=uuid4,
    )

    event_id: Mapped[UUID] = mapped_column(
        PG_UUID(as_uuid=True),
        nullable=False,
        unique=True,
    )

    correlation_id: Mapped[UUID | None] = mapped_column(
        PG_UUID(as_uuid=True),
        nullable=True,
        index=True,
    )

    system_id: Mapped[UUID] = mapped_column(
        PG_UUID(as_uuid=True),
        nullable=False,
        index=True,
    )

    recipient: Mapped[str] = mapped_column(
        String(255),
        nullable=False,
    )

    channel: Mapped[NotificationChannel] = mapped_column(
        Enum(
            NotificationChannel,
            name="notification_channel",
        ),
        nullable=False,
    )

    subject: Mapped[str] = mapped_column(
        String(255),
        nullable=False,
    )

    body: Mapped[str] = mapped_column(
        Text,
        nullable=False,
    )

    status: Mapped[NotificationStatus] = mapped_column(
        Enum(
            NotificationStatus,
            name="notification_status",
        ),
        nullable=False,
        default=NotificationStatus.CREATED,
    )

    created_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True),
        server_default=func.now(),
        nullable=False,
    )