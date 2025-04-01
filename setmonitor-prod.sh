#!/bin/bash

# shellcheck disable=SC2155
export UID=$(id -u)
export GID=$(id -g)

docker compose -f compose.local.monitoring.yaml up -d
docker-compose -f compose.local.monitoring.yaml up -d
