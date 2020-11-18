#!/bin/bash

echo "******************Remove all previous files******************"
if  [ ! -d  "/home/ubuntu/webapp/"  ]; then
echo  "No webapp"
else
sudo rm  -rf  /home/ubuntu/webapp
fi

if  [ ! -f  "/home/ubuntu/application.properties"  ]; then
echo  "No application.properties"
else
sudo rm  -rf  /home/ubuntu/application.properties
fi

if  [ ! -f  "/home/ubuntu/app.json"  ]; then
echo  "No app.json"
else
sudo rm  -rf  /home/ubuntu/app.json
fi

if  [ ! -f  "/home/ubuntu/infrastructure.json"  ]; then
echo  "No infrastructure.json"
else
sudo rm  -rf  /home/ubuntu/infrastructure.json
fi

if  [ ! -f  "/home/ubuntu/matrics.json"  ]; then
echo  "No matrics.json"
else
sudo rm  -rf  /home/ubuntu/matrics.json
fi
echo "******************Start deploy code******************"
echo "Excute user data"
sudo /var/lib/cloud/instance/scripts/part-001

echo "creating cloudwatch configuration file"
# echo '{
#   "logs": {
#     "logs_collected": {
#       "files": {
#         "collect_list": [
#           {
#             "file_path": "/opt/codedeploy-agent/webapp.log",
#             "log_group_name": "webapp"
#           }
#         ]
#       }
#     }
#   }
# }
# ' > /home/ubuntu/app.json

echo '{
  "metrics": {
    "namespace": "WEBAPP",
    "metrics_collected": {
      "statsd":{
         "service_address":":8125",
         "metrics_collection_interval":10,
         "metrics_aggregation_interval":60
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
          },
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

# echo '{
#    "metrics":{
#       "metrics_collected":{
#          "statsd":{
#             "service_address":":8125",
#             "metrics_collection_interval":10,
#             "metrics_aggregation_interval":60
#          }
#       }
#    }
# }
# ' > /home/ubuntu/matrics.json