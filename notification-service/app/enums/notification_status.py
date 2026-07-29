from enum import Enum


class NotificationStatus(str, Enum):
    CREATED = "CREATED"
    PROCESSING = "PROCESSING"
    SENT = "SENT"
    FAILED = "FAILED"