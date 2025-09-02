#!/bin/bash

CONTAINER_NAME="account_access_consent_db"
IMAGE_NAME="postgres:14"

if [ "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo "Container '$CONTAINER_NAME' is already running. No action needed. ✅"
    else
        echo "Container '$CONTAINER_NAME' exists but is stopped. Starting it now... 🚀"
        docker start $CONTAINER_NAME
        echo "Container '$CONTAINER_NAME' started successfully! ✨"
    fi
else
    echo "Container '$CONTAINER_NAME' does not exist. Creating and running a new one... 🐳"
    docker run -d \
      --name $CONTAINER_NAME \
      -e POSTGRES_DB=consent_db \
      -e POSTGRES_USER=postgres \
      -e POSTGRES_PASSWORD=secret \
      -p 5432:5432 \
      $IMAGE_NAME
    echo "Container '$CONTAINER_NAME' created and running successfully! 🎉"
fi

echo ""
echo "Current status of '$CONTAINER_NAME':"
docker ps -a --filter "name=$CONTAINER_NAME"
