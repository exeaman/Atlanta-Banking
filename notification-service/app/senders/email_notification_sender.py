import logging
from email.message import EmailMessage

import aiosmtplib

from app.core.config import settings
from app.models.notification import Notification
from app.senders.notification_sender import NotificationSender

logger = logging.getLogger(__name__)


class EmailNotificationSender(NotificationSender):

    async def send(
        self,
        notification: Notification,
    ) -> None:

        logger.info(
            "Sending EMAIL notification to '%s'",
            notification.recipient,
        )

        message = EmailMessage()

        message["From"] = settings.smtp_sender
        message["To"] = notification.recipient
        message["Subject"] = notification.subject or ""

        message.set_content(notification.body)

        await aiosmtplib.send(
            message,
            hostname=settings.smtp_host,
            port=settings.smtp_port,
            username=settings.smtp_username,
            password=settings.smtp_password,
            start_tls=settings.smtp_starttls,
            timeout=settings.smtp_timeout,
        )

        logger.info(
            "EMAIL notification sent successfully to '%s'",
            notification.recipient,
        )