#!/bin/bash

# echo "Excute user data"
# sudo /var/lib/cloud/instance/scripts/part-001

echo "Start springboot "
sudo nohup java -jar -Dspring.config.location=/home/ubuntu/application.properties /home/ubuntu/webapp/webapp-0.0.1-SNAPSHOT.jar >/dev/null 2>&1 &

echo "Start cloud watch"
sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/home/ubuntu/infrastructure.json

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/home/ubuntu/app.json

sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl -a fetch-config -m ec2 -s -c file:/home/ubuntu/matrics.json