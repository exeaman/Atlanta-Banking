from contextlib import asynccontextmanager

from fastapi import FastAPI

from app.api.health import router as health_router
from app.api.notification_controller import router as notification_router
from app.core.config import settings
from app.core.logging import configure_logging
from app.database.base import Base
from app.database.database import engine
from app.exceptions.global_exception_handler import register_exception_handlers

configure_logging()


@asynccontextmanager
async def lifespan(app: FastAPI):

    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

    yield


app = FastAPI(
    title=settings.app_name,
    version=settings.app_version,
    lifespan=lifespan,
)

register_exception_handlers(app)

app.include_router(health_router)
app.include_router(notification_router)