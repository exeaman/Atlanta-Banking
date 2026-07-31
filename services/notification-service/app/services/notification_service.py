from app.events.employee_created_event import EmployeeCreatedEvent
from app.mappers.notification_mapper import NotificationMapper
from app.repositories.notification_repository import NotificationRepository
from app.schemas.notification_response import NotificationResponse


class NotificationService:

    def __init__(
        self,
        notification_repository: NotificationRepository,
        notification_mapper: NotificationMapper,
    ):
        self.notification_repository = notification_repository
        self.notification_mapper = notification_mapper

    def handle_employee_created(
        self,
        event: EmployeeCreatedEvent,
    ) -> None:

        existing_notification = (
            self.notification_repository.find_by_event_id(
                event.metadata.event_id
            )
        )

        if existing_notification:
            return self.notification_mapper.to_notification_response(
                existing_notification
            )

        notification = self.notification_mapper.to_notification(event)

        saved_notification = self.notification_repository.save(
            notification
        )