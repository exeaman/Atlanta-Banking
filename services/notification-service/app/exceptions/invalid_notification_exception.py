from app.exceptions.notification_exception import NotificationException


class InvalidNotificationException(NotificationException):
    """
    Raised when a notification is invalid before sending.
    """

    def __init__(self, message: str):
        super().__init__(message)