#!/bin/sh
APP_NAME_ADMIN=litemall-admin-api

tpid=`ps -ef|grep $APP_NAME_ADMIN|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
        echo 'APP_NAME_ADMIN is running.'
else
        echo 'APP_NAME_ADMIN is NOT running.'
fi

APP_NAME_WX_API=litemall-wx-api

tpid=`ps -ef|grep $APP_NAME_ADMIN|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
        echo 'APP_NAME_WX_API is running.'
else
        echo 'APP_NAME_WX_API is NOT running.'
fi