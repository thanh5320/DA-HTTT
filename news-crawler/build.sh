#!/bin/bash
docker build -t alert-crawler-news-post:v1.0.1 .
#docker push 192.168.1.6:5555/alert-crawler-news-post:v1.0.$1
docker rm -f alert-tc2-crawler-news-post
docker run --name alert-tc2-crawler-news-post -v /etc/hosts:/etc/hosts:ro -d --restart unless-stopped alert-crawler-news-post:v1.0.1