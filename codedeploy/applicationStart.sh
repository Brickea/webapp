#!/bin/bash

# echo "Excute user data"
# sudo /var/lib/cloud/instance/scripts/part-001

echo "Start springboot"
sudo nohup java -jar -Dspring.config.location=/home/ubuntu/application.properties /home/ubuntu/webapp/webapp-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &