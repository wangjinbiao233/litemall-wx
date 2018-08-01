#!/bin/sh
APP_NAME_ADMIN=litemall-admin-api

tpid=`ps -ef|grep $APP_NAME_ADMIN|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 5
tpid=`ps -ef|grep $APP_NAME_ADMIN|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill APP_NAME_ADMIN Process!'
    kill -9 $tpid
else
    echo 'Stop APP_NAME_ADMIN Success!'
fi
APP_NAME_WX_API=litemall-wx-api

tpid=`ps -ef|grep $APP_NAME_WX_API|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop APP_NAME_WX_API Process...'
    kill -15 $tpid
fi
sleep 5
tpid=`ps -ef|grep $APP_NAME_WX_API|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill APP_NAME_WX_API Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi