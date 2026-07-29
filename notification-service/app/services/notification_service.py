from datetime import datetime

from app.enums.notification_status import NotificationStatus
from app.exceptions.notification_delivery_exception import (
    NotificationDeliveryException,
)
from app.exceptions.notification_exception import NotificationException
from app.factories.notification_sender_factory import NotificationSenderFactory
from app.mappers.notification_mapper import NotificationMapper
from app.models.notification import Notification
from app.repositories.notification_repository import NotificationRepository
from app.schemas.create_notification_request import CreateNotificationRequest
from app.schemas.notification_response import NotificationResponse



class NotificationService:

    def __init__(
        self,
        repository: NotificationRepository,
        sender_factory: NotificationSenderFactory,
        mapper: NotificationMapper,
    ):
        self._repository = repository
        self._sender_factory = sender_factory
        self._mapper = mapper

    async def send_notification(
        self,
        request: CreateNotificationRequest,
    ) -> NotificationResponse:

        notification = Notification(
            recipient=request.recipient,
            channel=request.channel,
            subject=request.subject,
            body=request.body,
        )

        notification = await self._repository.save(notification)

        sender = self._sender_factory.get_sender(
            notification.channel
        )

        try:
            await sender.send(notification)

            notification.status = NotificationStatus.SENT
            notification.sent_at = datetime.utcnow()

        except NotificationException as ex:

            notification.status = NotificationStatus.FAILED
            notification.failure_reason = (
                str(ex)[:500] if str(ex) else "Unknown error"
            )

            raise

        except Exception as ex:

            notification.status = NotificationStatus.FAILED
            notification.failure_reason = (
                str(ex)[:500] if str(ex) else "Unknown error"
            )

            raise NotificationDeliveryException(
                "Unexpected error while delivering notification."
            ) from ex

        finally:
            notification = await self._repository.save(notification)

        return self._mapper.to_response(notification)