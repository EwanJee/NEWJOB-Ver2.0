#!/bin/bash

# Docker Compose 빌드
docker-compose -f compose.prod.yaml build

# 이미지 태그 지정 (이미지 이름과 Docker Hub 저장소 이름 수정)
docker tag api ewanjeedocker/api:latest

# Docker Hub로 이미지 푸시
docker push ewanjeedocker/api:latest
