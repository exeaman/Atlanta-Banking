from uuid import UUID

from sqlalchemy import select
from sqlalchemy.orm import Session

from app.models.notification import Notification


class NotificationRepository:

    def __init__(self, db: Session):
        self.db = db

    def save(self, notification: Notification) -> Notification:
        self.db.add(notification)
        self.db.commit()
        self.db.refresh(notification)
        return notification

    def find_by_id(self, notification_id: UUID) -> Notification | None:
        return self.db.get(Notification, notification_id)

    def find_by_event_id(self, event_id: UUID) -> Notification | None:
        stmt = (
            select(Notification)
            .where(Notification.event_id == event_id)
        )

        return self.db.scalar(stmt)

    def find_by_correlation_id(
        self,
        correlation_id: UUID,
    ) -> list[Notification]:

        stmt = (
            select(Notification)
            .where(Notification.correlation_id == correlation_id)
            .order_by(Notification.created_at)
        )

        return list(self.db.scalars(stmt).all())