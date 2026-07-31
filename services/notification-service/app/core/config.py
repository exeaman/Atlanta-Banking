from os import getenv
from pathlib import Path

from pydantic_settings import BaseSettings, SettingsConfigDict

PROFILE = getenv("PROFILE", "local")

ROOT_DIR = Path(__file__).resolve().parents[4]


class Settings(BaseSettings):
    SERVER_PORT: int

    DATABASE_URL: str

    KAFKA_BOOTSTRAP_SERVERS: str
    KAFKA_EMPLOYEE_TOPIC: str
    KAFKA_CONSUMER_GROUP: str

    EUREKA_SERVER_URL: str

    model_config = SettingsConfigDict(
        env_file=ROOT_DIR / "config" / PROFILE / "notification.env",
        extra="ignore",
    )


settings = Settings()