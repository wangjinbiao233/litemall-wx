#!/bin/bash

# 请注意
# 1. 本脚本的作用是停止当前Spring Boot应用，然后再次部署，此外解压缩litemall-admin的静态文件
# 2. litemall-admin解压目录是 /home/ubuntu/deploy/litemall-admin/dist，
#    而这个目录也正是tomcat配置静态文件目录的路径（见1.5.3.5节）

svn update

mvn clean
mvn package

cp litemall-admin-api/target/litemall-admin-api-0.1.0.jar deploy/litemall-admin-api/litemall-admin-api.jar
cp litemall-wx-api/target/litemall-wx-api-0.1.0.jar deploy/litemall-wx-api/litemall-wx-api.jar
cp litemall-os-api/target/litemall-os-api-0.1.0.jar deploy/litemall-os-api/litemall-os-api.jar

cd litemall-admin
npm run build:prod

rm -rf /home/steve/deploy/litemall-admin/dist
mv /home/steve/eclipse-workspace/litemall/litemall-admin/dist /home/steve/deploy/litemall-admin/

#部署litemall-admin静态文件应用
#cd /home/steve/deploy/litemall-admin
#rm -rf dist
#mkdir dist
#tar -zxvf dist.tar -C dist
#cd .
#cd /home/steve/deploy
#部署三个Spring Boot应用
#如果服务已经启动，则尝试停止
sudo /etc/init.d/litemall-os-api stop
sudo /etc/init.d/litemall-wx-api stop
sudo /etc/init.d/litemall-admin-api stop

#部署Spring Boot应用成服务
sudo ln -f -s /home/steve/eclipse-workspace/litemall/deploy/litemall-os-api/litemall-os-api.jar /etc/init.d/litemall-os-api
sudo ln -f -s /home/steve/eclipse-workspace/litemall/deploy/litemall-wx-api/litemall-wx-api.jar /etc/init.d/litemall-wx-api
sudo ln -f -s /home/steve/eclipse-workspace/litemall/deploy/litemall-admin-api/litemall-admin-api.jar /etc/init.d/litemall-admin-api

#启动服务
sudo /etc/init.d/litemall-os-api restart
sudo /etc/init.d/litemall-wx-api restart
sudo /etc/init.d/litemall-admin-api restart
