import asyncio
import json
import logging

from aiokafka import AIOKafkaConsumer

from app.events.employee_created_event import EmployeeCreatedEvent
from app.services.notification_service import NotificationService

logger = logging.getLogger(__name__)


class EmployeeCreatedConsumer:

    def __init__(
        self,
        bootstrap_servers: str,
        topic: str,
        group_id: str,
        notification_service: NotificationService,
    ):
        self.notification_service = notification_service

        self.consumer = AIOKafkaConsumer(
            topic,
            bootstrap_servers=bootstrap_servers,
            group_id=group_id,
            auto_offset_reset="earliest",
            enable_auto_commit=True,
        )

    async def start(self) -> None:
        await self.consumer.start()

        logger.info("EmployeeCreatedConsumer started.")

        try:
            async for message in self.consumer:
                await self._handle_message(message.value)

        finally:
            await self.consumer.stop()

    async def stop(self) -> None:
        await self.consumer.stop()

    async def _handle_message(
        self,
        message: bytes,
    ) -> None:

        try:
            payload = json.loads(message.decode("utf-8"))

            event = EmployeeCreatedEvent.model_validate(payload)

            self.notification_service.handle_employee_created(
                event
            )

            logger.info(
                "Processed EmployeeCreatedEvent for employeeId=%s",
                event.employee_id,
            )

        except Exception:
            logger.exception(
                "Failed to process EmployeeCreatedEvent."
            )