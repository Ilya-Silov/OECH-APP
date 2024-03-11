#!/bin/sh


# URL репозитория на GitHub
REPO_URL="https://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@github.com/Ilya-Silov/OECH-APP"

# Директория назначения
DEST_DIR="./oech-app"

# Файл docker-compose
COMPOSE_FILE="compose-build.yaml"

# Клонирование репозитория из GitHub
git clone "$REPO_URL" "$DEST_DIR"

# Переход в директорию с репозиторием
cd "$DEST_DIR" || exit

# Запуск развертывания контейнеров с помощью docker-compose
docker compose -f "$COMPOSE_FILE" up -d