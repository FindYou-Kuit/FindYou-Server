#!/bin/bash

IS_BLUE_RUNNING=$(docker ps | grep findyou_blue)
export NGINX_CONF="/etc/nginx/nginx.conf"

# blue 가 실행 중이면 green 을 up
if [ -n "$IS_BLUE_RUNNING" ]; then
  echo "### BLUE => GREEN ####"

  echo ">>> green 컨테이너 실행"
  docker compose up -d findyou_green
  sleep 7

  echo ">>> health check 진행..."
  while true; do
    RESPONSE=$(curl http://localhost:9002/actuator/health | grep UP)
    if [ -n "$RESPONSE" ]; then
      echo ">>> green health check 성공! "
      break;
    fi
    sleep 3
  done;

  echo ">>> Nginx 설정 변경 (green)"
  sudo sed -i 's/set $ACTIVE_APP findyou_blue;/set $ACTIVE_APP findyou_green;/' $NGINX_CONF
  sudo nginx -s reload

  echo ">>> blue 컨테이너 종료"
  docker compose stop findyou_blue

# green 이 실행 중이면 blue 를 up
else
  echo "### GREEN => BLUE ####"

  echo ">>> blue 컨테이너 실행"
  docker compose up -d findyou_blue
  sleep 7

  echo ">>> health check 진행..."
  while true; do
    RESPONSE=$(curl http://localhost:9001/actuator/health | grep UP)
    if [ -n "$RESPONSE" ]; then
      echo ">>> blue health check 성공! "
      break;
    fi
    sleep 3
  done;

  echo ">>> Nginx 설정 변경 (blue)"
  sudo sed -i 's/set $ACTIVE_APP findyou_green;/set $ACTIVE_APP findyou_blue;/' $NGINX_CONF
  sudo nginx -s reload

  echo ">>> green 컨테이너 종료"
  docker compose stop findyou_green
fi

echo ">>> 사용하지 않는 도커 이미지들 정리"
docker image prune -a -f