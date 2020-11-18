#!/bin/bash

echo "******************Remove all previous files******************"
sudo rm -r /home/ubuntu/webapp
sudo rm /home/ubuntu/application.properties
sudo rm /home/ubuntu/app.json
sudo rm /home/ubuntu/infrastructure.json

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

echo '{
   "metrics":{
      "metrics_collected":{
         "statsd":{
            "service_address":":8080",
            "metrics_collection_interval":10,
            "metrics_aggregation_interval":60
         }
      }
   }
}
' > matrics.json