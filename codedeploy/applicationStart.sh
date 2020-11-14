#!/bin/bash

echo "Excute user data"
sudo /var/lib/cloud/instance/scripts/part-001

echo "Start springboot"
# sudo nohup java -jar -Dspring.config.location=application.properties webapp/webapp-0.0.1-SNAPSHOT.jar &