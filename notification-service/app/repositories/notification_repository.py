from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.notification import Notification


class NotificationRepository:

    def __init__(self, session: AsyncSession):
        self._session = session

    async def save(
        self,
        notification: Notification,
    ) -> Notification:

        # Register this entity with SQLAlchemy's Unit of Work.
        #
        # No SQL is executed yet.
        # SQLAlchemy simply starts tracking this object.
        self._session.add(notification)

        # Flush all pending INSERT/UPDATE/DELETE statements
        # to the database and commit the transaction.
        await self._session.commit()

        # Reload the entity from the database.
        #
        # This is important because the database may have
        # generated values such as:
        #   - id
        #   - created_at
        #   - trigger-generated values
        await self._session.refresh(notification)

        return notification

    async def find_by_id(
        self,
        notification_id,
    ) -> Notification | None:

        # Build the SQL query.
        #
        # Still no SQL execution here.
        statement = select(Notification).where(
            Notification.id == notification_id
        )

        # Execute the SQL against PostgreSQL.
        result = await self._session.execute(statement)

        # Convert the SQLAlchemy Result object into either:
        #   Notification
        #   None
        return result.scalar_one_or_none()