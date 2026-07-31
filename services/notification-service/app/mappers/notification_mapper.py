from app.enums.notification_channel import NotificationChannel
from app.enums.notification_status import NotificationStatus
from app.events.employee_created_event import EmployeeCreatedEvent
from app.models.notification import Notification
from app.schemas.notification_response import NotificationResponse


class NotificationMapper:

    @staticmethod
    def to_notification(event: EmployeeCreatedEvent) -> Notification:
        return Notification(
            event_id=event.metadata.event_id,
            correlation_id=event.metadata.correlation_id,
            system_id=event.system_id,

            recipient=event.email,

            channel=NotificationChannel.EMAIL,

            subject="Welcome to Atlanta Banking",

            body=(
                f"Hello {event.first_name},\n\n"
                "Your employee account has been successfully created.\n\n"
                f"Employee ID: {event.employee_id}\n"
                f"Username: {event.username}\n"
                f"Department: {event.department.value}\n\n"
                "Please contact your administrator if you have any questions."
            ),

            status=NotificationStatus.CREATED,
        )

    @staticmethod
    def to_notification_response(
            notification: Notification,
    ) -> NotificationResponse:
        return NotificationResponse.model_validate(notification)