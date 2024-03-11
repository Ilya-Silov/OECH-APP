#!/bin/sh


# URL репозитория на GitHub
REPO_URL="https://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n@github.com/Ilya-Silov/OECH-APP"

# Директория назначения
DEST_DIR="./oech-app"

# Файл docker-compose
COMPOSE_FILE="compose-build.yaml"

# Клонирование репозитория из GitHub
git clone "$REPO_URL" "$DEST_DIR"

# Переход в директорию с репозиторием
cd "$DEST_DIR" || exit

# Запуск развертывания контейнеров с помощью docker-compose
docker-compose -f "$COMPOSE_FILE" up -d