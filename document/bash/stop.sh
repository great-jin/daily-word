#!/bin/sh
APP_NAME=daily-word-client-0.1-SNAPSHOT.jar

PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
echo Application is already stopped
else
echo kill $PID
kill $PID
fi