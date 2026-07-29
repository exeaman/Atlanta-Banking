from app.models.notification import Notification
from app.schemas.notification_response import NotificationResponse


class NotificationMapper:

    @staticmethod
    def to_response(
        notification: Notification,
    ) -> NotificationResponse:

        # Convert the database entity into the API response.
        # This is the object FastAPI will serialize into JSON.
        return NotificationResponse(
            id=notification.id,
            recipient=notification.recipient,
            channel=notification.channel,
            subject=notification.subject,
            body=notification.body,
            status=notification.status,
            failure_reason=notification.failure_reason,
            created_at=notification.created_at,
            sent_at=notification.sent_at,
        )