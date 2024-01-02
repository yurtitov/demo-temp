#!/usr/bin/env bash

echo "Start deploy"
docker stop target-app-1
docker rm target-app-1

docker compose down
docker compose up -d