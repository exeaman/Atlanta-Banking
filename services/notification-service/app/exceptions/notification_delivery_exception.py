from app.exceptions.notification_exception import NotificationException


class NotificationDeliveryException(NotificationException):
    """
    Raised when a notification cannot be delivered.
    """

    def __init__(self, message: str = "Failed to deliver notification"):
        super().__init__(message)