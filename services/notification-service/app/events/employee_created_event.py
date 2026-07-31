from uuid import UUID

from pydantic import BaseModel, ConfigDict, Field

from app.enums.department import Department
from app.events.event_metadata import EventMetadata


class EmployeeCreatedEvent(BaseModel):
    model_config = ConfigDict(
        populate_by_name=True
    )

    metadata: EventMetadata

    system_id: UUID = Field(alias="systemId")
    employee_id: str = Field(alias="employeeId")
    username: str
    first_name: str = Field(alias="firstName")
    last_name: str = Field(alias="lastName")
    email: str
    department: Department