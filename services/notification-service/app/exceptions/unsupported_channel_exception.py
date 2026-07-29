from app.enums.notification_channel import NotificationChannel
from app.exceptions.notification_exception import NotificationException


class UnsupportedChannelException(NotificationException):
    """
    Raised when no sender exists for a notification channel.
    """

    def __init__(self, channel: NotificationChannel):
        super().__init__(
            f"Unsupported notification channel: {channel.value}"
        )