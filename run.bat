@echo off

REM URL репозитория на GitHub
SET REPO_URL=https://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@github.com/Ilya-Silov/OECH-APP

REM Директория назначения
SET DEST_DIR=.\oech-app

REM Файл docker-compose
SET COMPOSE_FILE=compose-build.yaml

REM Клонирование репозитория из GitHub
git clone "%REPO_URL%" "%DEST_DIR%"

REM Переход в директорию с репозиторием
cd /d "%DEST_DIR%" || exit

REM Запуск развертывания контейнеров с помощью docker-compose
docker compose -f "%COMPOSE_FILE%" up -d