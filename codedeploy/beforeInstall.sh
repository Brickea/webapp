#!/bin/bash

echo "******************Start deploy code******************"
echo "Excute user data"
sudo /var/lib/cloud/instance/scripts/part-001

echo "creating cloudwatch configuration file"
echo '{
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/opt/codedeploy-agent/webapp.log",
            "log_group_name": "webapp"
          }
        ]
      }
    }
  }
}
' > /home/ubuntu/app.json

echo '{
  "metrics": {
    "metrics_collected": {
      "cpu": {
        "resources": [
          "*"
        ],
        "measurement": [
          "usage_active"
        ],
        "totalcpu": true
      },
      "mem": {
         "measurement": [
           "used_percent"
        ]
      }
}
  },
  "logs": {
    "logs_collected": {
      "files": {
        "collect_list": [
          {
            "file_path": "/opt/aws/amazon-cloudwatch-agent/logs/amazon-cloudwatch-agent.log",
            "log_group_name": "amazon-cloudwatch-agent"
          }
        ]
      }
    }
  }
}
' > /home/ubuntu/infrastructure.json