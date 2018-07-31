#!/bin/bash

# 请注意
# 1. 本脚本的作用是停止当前Spring Boot应用，然后再次部署，此外解压缩litemall-admin的静态文件
# 2. litemall-admin解压目录是 /home/ubuntu/deploy/litemall-admin/dist，
#    而这个目录也正是tomcat配置静态文件目录的路径（见1.5.3.5节）

svn update

mvn clean
mvn package

cp litemall-admin-api/target/litemall-admin-api-0.1.0.jar deploy/litemall-admin-api.jar
cp litemall-wx-api/target/litemall-wx-api-0.1.0.jar deploy/litemall-wx-api.jar
cp litemall-os-api/target/litemall-os-api-0.1.0.jar deploy/litemall-os-api.jar

cd litemall-admin
#npm install
npm run build:prod
