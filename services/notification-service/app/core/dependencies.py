from sqlalchemy.orm import Session

from app.mappers.notification_mapper import NotificationMapper
from app.repositories.notification_repository import NotificationRepository
from app.services.notification_service import NotificationService


def get_notification_service(db: Session) -> NotificationService:
    repository = NotificationRepository(db)
    mapper = NotificationMapper()

    return NotificationService(
        notification_repository=repository,
        notification_mapper=mapper,
    )