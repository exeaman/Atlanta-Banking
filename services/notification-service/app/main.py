from contextlib import asynccontextmanager
import asyncio

from fastapi import FastAPI

from app import api
from app.api import health
from app.consumers.employee_created_consumer import EmployeeCreatedConsumer
from app.core.config import settings, PROFILE
from app.core.dependencies import get_notification_service
from app.database.initializer import initialize_database
from app.database.session import SessionLocal

#if PROFILE == "local":
#    initialize_database()

@asynccontextmanager
async def lifespan(app: FastAPI):

    db = SessionLocal()

    service = get_notification_service(db)

    consumer = EmployeeCreatedConsumer(
        bootstrap_servers=settings.KAFKA_BOOTSTRAP_SERVERS,
        topic=settings.KAFKA_EMPLOYEE_TOPIC,
        group_id=settings.KAFKA_CONSUMER_GROUP,
        notification_service=service,
    )

    task = asyncio.create_task(
        consumer.start()
    )

    yield

    await consumer.stop()

    task.cancel()

    db.close()


app = FastAPI(
    lifespan=lifespan
)

# api.include_router(health.router)