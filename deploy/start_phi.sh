#!/bin/sh

nohup /home/steve/jdk1.8.0_171/bin/java -jar /home/steve/deploy/litemall-admin-api.jar --spring.profiles.active=prod > /home/steve/admin.log 2>&1 &
nohup /home/steve/jdk1.8.0_171/bin/java -jar /home/steve/deploy/litemall-wx-api.jar --spring.profiles.active=prod > /home/steve/wx-api.log 2>&1 &

echo Start Success!
