#!/bin/bash

echo "Excute user data"
sudo /var/lib/cloud/instance/scripts/part-001

echo "Start springboot"
java -jar webapp-0.0.1-SNAPSHOT.jar -Dspring.config.location=/home/ubuntu/application.properties