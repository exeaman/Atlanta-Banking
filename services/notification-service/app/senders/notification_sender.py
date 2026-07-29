from abc import ABC, abstractmethod

from app.models.notification import Notification


class NotificationSender(ABC):

    @abstractmethod
    async def send(self, notification: Notification) -> None:
        """
        Sends the given notification.
        """
        raise NotImplementedError