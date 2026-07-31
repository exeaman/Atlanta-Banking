FROM python:3.12-slim

# Prevent Python from writing .pyc files
ENV PYTHONDONTWRITEBYTECODE=1

# Ensure stdout/stderr are unbuffered
ENV PYTHONUNBUFFERED=1

WORKDIR /app

# Install uv
COPY --from=ghcr.io/astral-sh/uv:latest /uv /usr/local/bin/uv

# Copy dependency files first (better Docker cache)
COPY pyproject.toml uv.lock ./

# Install dependencies
RUN uv sync --frozen

# Copy application source
COPY . .

EXPOSE 8000

CMD ["uv", "run", "python", "-m", "uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]