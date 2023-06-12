#!/bin/bash

set -e # 遇到错误即退出

# 定义 rocketpt 和 rocketpt-new 的文件名
JAR_NAME="rocketpt.jar"
JAR_NAME_NEW="rocketpt-new.jar"


# 获取脚本所在目录
SCRIPT_DIR=$(cd $(dirname ${BASH_SOURCE[0]}); pwd)

cd $SCRIPT_DIR


# 检查 jar 是否存在
if [ ! -f "$JAR_NAME_NEW" ] ; then
  echo "$JAR_NAME_NEW not found! Aborting."
  exit 1
fi

# 查找 rocketpt 的进程pid，并尝试优雅地关闭
bash run.sh stop


# 备份 rocketpt.jar 并替换为 rocketpt-new.jar
if [ -f "$JAR_NAME" ] ; then
  # 计算备份文件名（带时间戳）
  TIMESTAMP=`date "+%Y%m%d_%H%M%S"`
  BACKUP_NAME="$JAR_NAME.$TIMESTAMP.jar"

  if [ ! -d "backup" ]; then
    mkdir backup
  fi
  # 将备份文件移动到 backup 目录下
  echo "备份文件 backup/$BACKUP_NAME"
  mv "$JAR_NAME" "backup/$BACKUP_NAME"
fi
mv "$JAR_NAME_NEW" "$JAR_NAME"





# 启动应用
start() {

  echo "Starting application..."
  bash run.sh restart
  echo "started java process ${JAR_NAME}"
  echo "Application started."
}


# 启动 rocketpt
start

