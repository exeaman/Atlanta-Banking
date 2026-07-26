# Atlanta Banking - Infrastructure

This directory contains the complete infrastructure required to run the Atlanta Banking microservices locally.

Application services (Identity, Audit, Gateway, Eureka, etc.) are **not** containerized. They are intended to be run
from the IDE for easier debugging and development.

---

## Architecture

```
                    Docker
┌─────────────────────────────────────────┐
│ PostgreSQL                  :5432       │
│ Apache Kafka (KRaft)        :9092       │
│ Kafka UI                    :9999       │
└─────────────────────────────────────────┘

                    IntelliJ
┌─────────────────────────────────────────┐
│ Eureka Server               :8761       │
│ API Gateway                 :8080       │
│ Identity Service            :8083       │
│ Audit Service               :8088       │
│ Future Microservices        :808x       │
└─────────────────────────────────────────┘
```

---

# Prerequisites

Install:

* Docker Engine (or Docker Desktop)
* Docker Compose v2

Verify installation:

```bash
docker --version
docker compose version
```

---

# Project Structure

```
infrastructure/
├── docker-compose.yml
├── .env
├── postgres/
│   └── init/
│       └── 01-init.sql
└── README.md
```

---

# Services

## PostgreSQL

Image

```
postgres:17
```

Host Port

```
5432
```

Purpose

* Primary relational database
* One database per microservice
* Persistent storage using Docker volumes

Current databases

| Database    | Service          |
|-------------|------------------|
| identity_db | Identity Service |
| audit_db    | Audit Service    |

---

## Apache Kafka

Image

```
apache/kafka:4.1.0
```

Mode

```
KRaft (No ZooKeeper)
```

Host Port

```
9092
```

Purpose

* Event streaming
* Asynchronous communication between services

---

## Kafka UI

Image

```
provectuslabs/kafka-ui
```

Host Port

```
9999
```

Access

```
http://localhost:9999
```

Purpose

* Browse topics
* Publish messages
* View partitions
* Inspect offsets
* Debug Kafka

---

# Starting Infrastructure

From this directory:

```bash
docker compose up -d
```

---

# Stopping Infrastructure

```bash
docker compose down
```

---

# Stop and Remove Containers

Containers only

```bash
docker compose down
```

Containers + Networks + Volumes

```bash
docker compose down -v
```

> **Warning:** `-v` permanently deletes PostgreSQL and Kafka data.

---

# View Running Containers

```bash
docker ps
```

---

# View Logs

All services

```bash
docker compose logs
```

Single service

```bash
docker compose logs postgres
docker compose logs kafka
docker compose logs kafka-ui
```

Follow logs

```bash
docker compose logs -f kafka
```

---

# Restart Infrastructure

```bash
docker compose restart
```

Restart one service

```bash
docker compose restart kafka
```

---

# Environment Variables

Configuration is stored in `.env`.

Example:

```
POSTGRES_PORT=5432
KAFKA_PORT=9092
KAFKA_UI_PORT=9999
```

Avoid hardcoding ports or credentials inside `docker-compose.yml`.

---

# Volumes

Persistent Docker volumes:

```
postgres-data
kafka-data
```

These preserve data between container restarts.

---

# Networking

All infrastructure containers communicate through the internal Docker bridge network.

Application services running from IntelliJ connect using:

```
localhost
```

Container-to-container communication uses Docker service names.

Example:

```
kafka:29092
```

---

# Kafka Listeners

Kafka exposes two listeners.

### Host Applications

```
localhost:9092
```

Used by:

* Identity Service
* Audit Service
* Any Spring Boot application running on the host

---

### Docker Containers

```
kafka:29092
```

Used by:

* Kafka UI
* Future Dockerized infrastructure

This separation allows host applications and Docker containers to communicate with the same broker without networking
conflicts.

---

# Database Ownership

Atlanta Banking follows the **Database per Service** pattern.

Each microservice owns its own database.

Example

| Service  | Database    | Schema   |
|----------|-------------|----------|
| Identity | identity_db | identity |
| Audit    | audit_db    | audit    |

No service accesses another service's database directly.

---

# Development Workflow

Start infrastructure

```bash
cd infrastructure
docker compose up -d
```

Run services from IntelliJ

```
✓ Eureka
✓ API Gateway
✓ Identity
✓ Audit
```

Stop infrastructure

```bash
docker compose down
```

---

# Future Infrastructure

The following services will be added as the project evolves:

* Redis
* Zipkin
* Prometheus
* Grafana

They will be managed by the same Docker Compose configuration.

---

# Design Principles

* Infrastructure runs in Docker.
* Application services run from the IDE.
* One database per microservice.
* Official Docker images preferred.
* Configuration via `.env`.
* Persistent storage using Docker volumes.
* Infrastructure should start with a single command.

```
docker compose up -d
```
