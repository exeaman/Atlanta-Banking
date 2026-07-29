from app.enums.notification_channel import NotificationChannel
from app.senders.email_notification_sender import EmailNotificationSender
from app.senders.notification_sender import NotificationSender


class NotificationSenderFactory:

    def __init__(
        self,
        email_sender: EmailNotificationSender,
    ):
        self._senders: dict[
            NotificationChannel,
            NotificationSender
        ] = {
            NotificationChannel.EMAIL: email_sender,
        }

    def get_sender(
        self,
        channel: NotificationChannel,
    ) -> NotificationSender:

        sender = self._senders.get(channel)

        if sender is None:
            raise ValueError(
                f"No sender configured for channel '{channel.value}'."
            )

        return sender