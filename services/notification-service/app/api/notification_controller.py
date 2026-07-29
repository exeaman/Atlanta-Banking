from fastapi import APIRouter, Depends, status

from app.schemas.create_notification_request import CreateNotificationRequest
from app.schemas.notification_response import NotificationResponse
from app.services.notification_service import NotificationService
from app.dependencies import get_notification_service

router = APIRouter(
    prefix="/notifications",
    tags=["Notifications"],
)


@router.post(
    "",
    response_model=NotificationResponse,
    status_code=status.HTTP_201_CREATED,
)
async def send_notification(
    request: CreateNotificationRequest,
    service: NotificationService = Depends(get_notification_service),
):

    # Controller responsibility:
    # - Receive HTTP request
    # - Delegate to the service
    # - Return the service's response
    return await service.send_notification(request)