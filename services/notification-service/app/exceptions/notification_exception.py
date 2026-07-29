class NotificationException(Exception):
    """
    Base exception for all notification-related errors.
    """

    def __init__(self, message: str):
        super().__init__(message)