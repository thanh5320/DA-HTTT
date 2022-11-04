#!/bin/bash
ssh noron@192.168.1.6 'docker pull 192.168.1.6:5555/thanh-demo:v1.0.'$1
ssh noron@192.168.1.6 'docker rm -f thanh-demo'
ssh noron@192.168.1.6 'docker run --name thanh-demo -v /etc/hosts:/etc/hosts:ro -d --net=host --restart unless-stopped 192.168.1.6:5555/thanh-demo:v1.0.'$1
