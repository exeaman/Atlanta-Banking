from datetime import UTC, datetime

from pydantic import BaseModel


class ValidationErrorDetail(BaseModel):
    field: str
    message: str


class ErrorResponse(BaseModel):
    timestamp: datetime = datetime.now(UTC)
    status: int
    error: str
    message: str
    path: str
    errors: list[ValidationErrorDetail] | None = None