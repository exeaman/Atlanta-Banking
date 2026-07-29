import logging
from datetime import UTC, datetime

from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse

from app.exceptions.not_found_exception import NotFoundException
from app.schemas.error_response import (
    ErrorResponse,
    ValidationErrorDetail,
)

logger = logging.getLogger(__name__)


def register_exception_handlers(app: FastAPI):

    @app.exception_handler(NotFoundException)
    async def handle_not_found(
        request: Request,
        exc: NotFoundException,
    ):
        return JSONResponse(
            status_code=404,
            content=ErrorResponse(
                timestamp=datetime.now(UTC),
                status=404,
                error="Not Found",
                message=exc.message,
                path=request.url.path,
            ).model_dump(mode="json"),
        )

    @app.exception_handler(RequestValidationError)
    async def handle_validation_error(
        request: Request,
        exc: RequestValidationError,
    ):
        errors = [
            ValidationErrorDetail(
                field=".".join(map(str, error["loc"][1:])),
                message=error["msg"],
            )
            for error in exc.errors()
        ]

        return JSONResponse(
            status_code=422,
            content=ErrorResponse(
                timestamp=datetime.now(UTC),
                status=422,
                error="Validation Error",
                message="Request validation failed.",
                path=request.url.path,
                errors=errors,
            ).model_dump(mode="json"),
        )

    @app.exception_handler(Exception)
    async def handle_exception(
        request: Request,
        exc: Exception,
    ):
        logger.exception("Unhandled exception")

        return JSONResponse(
            status_code=500,
            content=ErrorResponse(
                timestamp=datetime.now(UTC),
                status=500,
                error="Internal Server Error",
                message="An unexpected error occurred.",
                path=request.url.path,
            ).model_dump(mode="json"),
        )