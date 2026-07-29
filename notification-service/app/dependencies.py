from sqlalchemy.ext.asyncio import AsyncSession

from app.database.database import get_session
from app.factories.notification_sender_factory import NotificationSenderFactory
from app.mappers.notification_mapper import NotificationMapper
from app.repositories.notification_repository import NotificationRepository
from app.senders.email_notification_sender import EmailNotificationSender
from app.services.notification_service import NotificationService
from fastapi import Depends

def get_notification_service(
    session: AsyncSession = Depends(get_session),
) -> NotificationService:

    # Lowest level dependency
    repository = NotificationRepository(session)

    # Stateless helper
    mapper = NotificationMapper()

    # Strategy implementation
    email_sender = EmailNotificationSender()

    # Factory knows about all senders
    sender_factory = NotificationSenderFactory(
        email_sender
    )

    # Finally build the service
    return NotificationService(
        repository,
        sender_factory,
        mapper,
    )