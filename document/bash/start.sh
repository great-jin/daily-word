#!/bin/bash
APP_NAME=daily-word-client-0.1-SNAPSHOT.jar

# 进程存在先停止
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
echo 'Stop Process...'
kill -9 $tpid
fi

# 检查进程是否已结束
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
echo 'Stop Process...'
kill -9 $tpid
else
echo 'Stop Procecss Successfully!'
echo 'Start Procecss...'

# 后台启动程序
nohup java -jar $(pwd)/$APP_NAME --spring.config.additional.location=$(pwd)/config/ > info.log 2>&1 &
fi
