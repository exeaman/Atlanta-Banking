from functools import lru_cache

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    app_name: str = "Atlanta Notification Service"
    app_version: str = "1.0.0"
    environment: str = "dev"

    host: str = "127.0.0.1"
    port: int = 8000

    postgres_host: str = "localhost"
    postgres_port: int = 5432
    postgres_database: str = "notification_db"
    postgres_username: str = "postgres"
    postgres_password: str = "postgres"

    kafka_bootstrap_servers: str = "localhost:9092"

    smtp_host: str = "localhost"
    smtp_port: int = 1025
    smtp_username: str = ""
    smtp_password: str = ""
    smtp_starttls: bool = True
    smtp_timeout: int = 10
    smtp_sender: str = ""

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        case_sensitive=False,
    )
    log_level: str = "DEBUG"
    @property
    def database_url(self) -> str:
        return (
            f"postgresql+psycopg://"
            f"{self.postgres_username}:"
            f"{self.postgres_password}@"
            f"{self.postgres_host}:"
            f"{self.postgres_port}/"
            f"{self.postgres_database}"
        )


@lru_cache
def get_settings() -> Settings:
    return Settings()


settings = get_settings()
