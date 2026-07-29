# Atlanta Banking – Notification Service

The Notification Service is an infrastructure microservice responsible for delivering notifications across different communication channels.

The initial implementation focuses on **Email notifications via SMTP** and is designed with extensibility in mind so that additional channels such as SMS, Push Notifications or WhatsApp can be added without changing the business layer.

---

## Features

- REST API for sending notifications
- Email delivery using Gmail SMTP
- Notification persistence in PostgreSQL
- Factory + Strategy pattern for channel selection
- Asynchronous implementation using FastAPI
- Global exception handling
- Structured logging
- OpenAPI (Swagger) documentation
- Notification status tracking
    - `CREATED`
    - `SENT`
    - `FAILED`

---

## Tech Stack

- Python 3.12
- FastAPI
- SQLAlchemy (Async)
- PostgreSQL
- Alembic
- Pydantic v2
- aiosmtplib
- Uvicorn

---

## Architecture

```
                HTTP Request
                      │
                      ▼
          Notification Controller
                      │
                      ▼
          Notification Service
             │               │
             │               ▼
             │     NotificationSenderFactory
             │               │
             │               ▼
             │      EmailNotificationSender
             │               │
             │               ▼
             │            Gmail SMTP
             │
             ▼
      Notification Repository
             │
             ▼
         PostgreSQL
```

---

## Project Structure

```
app
├── controllers
├── core
├── database
├── enums
├── exceptions
├── factories
├── mappers
├── models
├── repositories
├── schemas
├── senders
├── services
└── main.py
```

---

## API

### Send Notification

```
POST /notifications
```

Example Request

```json
{
  "recipient": "user@example.com",
  "channel": "EMAIL",
  "subject": "Welcome",
  "body": "Welcome to Atlanta Banking."
}
```

Example Response

```json
{
  "id": "...",
  "recipient": "user@example.com",
  "channel": "EMAIL",
  "status": "SENT",
  "created_at": "...",
  "sent_at": "..."
}
```

---

## Configuration

Configuration is provided through environment variables.

Example:

```properties
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
POSTGRES_DATABASE=notification_db
POSTGRES_USERNAME=postgres
POSTGRES_PASSWORD=postgres

SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USERNAME=your-email@gmail.com
SMTP_PASSWORD=your-app-password
SMTP_SENDER=your-email@gmail.com

LOG_LEVEL=DEBUG
```

---

## Running the Service

Install dependencies

```bash
pip install -r requirements.txt
```

Run the application

```bash
uvicorn app.main:app --reload
```

Swagger UI

```
http://localhost:8000/docs
```

---

## Notification Lifecycle

```
Request Received
        │
        ▼
Notification Created
        │
        ▼
Persist (CREATED)
        │
        ▼
Select Sender
        │
        ▼
Send via SMTP
        │
   ┌────┴────┐
   ▼         ▼
SENT      FAILED
   │         │
   └────┬────┘
        ▼
Persist Final Status
```

---

## Design Principles

- Layered Architecture
- Dependency Injection
- Repository Pattern
- Factory Pattern
- Strategy Pattern
- Separation of Concerns
- Configuration Driven
- Asynchronous I/O

---

## Current Supported Channels

| Channel | Status |
|---------|--------|
| Email | ✅ Implemented |
| SMS | 🚧 Planned |
| Push Notification | 🚧 Planned |

---

## Future Enhancements

- Retry policy
- Background job processing
- Kafka integration
- Multiple email providers
- HTML email templates
- Metrics & monitoring

---

## Role within Atlanta Banking

The Notification Service is an infrastructure service used by other microservices to deliver notifications.

Typical flow:

```
Identity Service
        │
Employee Created
        │
        ▼
Notification Service
        │
        ▼
Email Delivered
```

The service is intentionally designed to remain independent of business logic. It focuses solely on reliable notification delivery while allowing upstream services to publish notification requests.