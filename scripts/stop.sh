#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="dowe.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

echo "$TIME_NOW > stop.sh 스크립트 실행" >> $DEPLOY_LOG

# 현재 구동 중인 애플리케이션 pid 확인
CURRENT_PID=$(pgrep -f "$JAR_FILE")

# 프로세스가 켜져 있으면 종료
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션 없음" >> $DEPLOY_LOG
else
  echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료" >> $DEPLOY_LOG

  MAX_RETRIES=3
  RETRY_COUNT=0

  while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
    kill -15 "$CURRENT_PID"
    echo "$TIME_NOW > SIGTERM($RETRY_COUNT) 시도, 5초 대기..." >> DEPLOY_LOG

    sleep 5

    CURRENT_PID=$(pgrep -f "$JAR_FILE")

    if [ -z "$CURRENT_PID" ]; then
      echo "$TIME_NOW > 애플리케이션 정상 종료" >> $DEPLOY_LOG
      break
    else
      echo "$TIME_NOW > 애플리케이션 미종료" >> $DEPLOY_LOG
    fi

    RETRY_COUNT=$((RETRY_COUNT+1))
  done

  if [ -n "$CURRENT_PID" ]; then
    echo "$TIME_NOW > 최대 재시도 회수 초과 -> 강제 종료 시도">> $DEPLOY_LOG
    kill -9 "$CURRENT_PID"
    echo "$TIME_NOW > 애플리케이션 강제 종료" >> $DEPLOY_LOG
  fi
fi