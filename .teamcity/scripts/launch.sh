#!/usr/bin/env bash

echo "Start deploy"
docker compose down
docker compose up -d