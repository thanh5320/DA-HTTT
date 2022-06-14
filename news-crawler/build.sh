#!/bin/bash
docker build -t 192.168.1.6:5555/alert-crawler-news-post:v1.0.$1 .
docker push 192.168.1.6:5555/alert-crawler-news-post:v1.0.$1