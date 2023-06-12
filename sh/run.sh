#!/bin/bash
#deploy shell script by chatgpt 2023-03-17

# 设置JVM参数
JVM_ARGS="-Xmx3024m -Xms3024m --add-opens=java.base/java.lang=ALL-UNNAMED"
APP_ARGS="--spring.profiles.active=prod"

# 获取脚本所在目录
SCRIPT_DIR=$(
  cd $(dirname ${BASH_SOURCE[0]})
  pwd
)

#run directory   echo $! > pid
APP_HOME=$SCRIPT_DIR

# jar应用名称
JAR_NAME="rocketpt.jar"

# 停止等待时间，单位：秒
STOP_WAIT_TIME=110

# jar文件路径
JAR_FILE="$APP_HOME/$JAR_NAME"

PID_FILE=${APP_HOME}/pid

#Absolute path to start log
LOG_FILE_PATH=${APP_HOME}/logs/start.log

# Create non-existent directory
mkdir -p ${APP_HOME}/logs

# 判断jar文件是否存在
if [[ ! -f "$JAR_FILE" ]]; then
  echo "warning: Jar文件不存在：$JAR_FILE"

fi

# 命令参数
COMMAND=$1

# 启动应用
start() {
  # 检查应用是否已经在运行
  if [[ -f "$PID_FILE" ]]; then
    PID=$(cat "$PID_FILE")
    if ps -p $PID >/dev/null; then
      echo "Application is already running, PID=$PID"
      return
    fi
    # PID文件对应进程不存在，则删除PID文件
    rm -f "$PID_FILE"
  fi

  echo "Starting application..."
  nohup java ${JVM_ARGS} -jar ${JAR_FILE} ${APP_ARGS} >>${LOG_FILE_PATH} 2>&1 &
  echo "started java process ${JAR_NAME}"

  # 获取并记录应用的进程ID
  PID=$!
  echo $PID >"$PID_FILE"
  echo "Recorded the PID ($PID) to $PID_FILE."
}

debug() {
  echo "starting java process ${JAR_NAME}"
  nohup java ${JVM_ARGS} -jar ${JAR_FILE} ${APP_ARGS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 >>${LOG_FILE_PATH} 2>&1 &
  # echo $! >${PID_FILE}
  echo "started java process ${JAR_NAME}"
}

# 停止应用
stop() {
  if [[ -f "$PID_FILE" ]]; then
    PID=$(cat "$PID_FILE")
    if ps -p $PID >/dev/null; then
      echo "Stopping application (PID=$PID)..."
      kill $PID
      # 每秒检查$STOP_WAIT_TIME秒是否已经停止
      for ((i = 1; i <= STOP_WAIT_TIME; i++)); do
        if ! ps -p $PID >/dev/null; then
          echo "Application stopped. (PID=$PID)"
          # 删除PID文件
          rm -f "$PID_FILE"
          return
        fi
        sleep 1
      done
      echo "Force killing application (PID=$PID)..."
      kill -9 $PID
      echo "Application force killed. (PID=$PID)"
      # 删除PID文件
      rm -f "$PID_FILE"
    else
      echo "Application is not running."
      # 删除PID文件
      rm -f "$PID_FILE"
    fi
  else
    echo "Application is not running."
  fi
}

# 重启应用
restart() {
  stop
  start
}

# 查看应用日志
log() {
  tail -f -n600 ${LOG_FILE_PATH}
}

usage() {
  echo "Usage: $PROG_NAME {start|stop|restart|log|deploy|debug}"
  exit 1
}

# 根据命令参数执行相应操作
case $COMMAND in
start)
  start
  ;;
stop)
  stop
  ;;
restart)
  restart
  ;;
log)
  log
  ;;
*)
  usage
  ;;
esac

exit 0
