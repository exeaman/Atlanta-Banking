from enum import Enum


class Department(str, Enum):
    RETAIL_BANKING = "RETAIL_BANKING"
    CORPORATE_BANKING = "CORPORATE_BANKING"
    LOANS = "LOANS"
    CARDS = "CARDS"
    OPERATIONS = "OPERATIONS"
    IT = "IT"
    HR = "HR"
    FINANCE = "FINANCE"