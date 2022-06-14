#!/bin/bash
ssh noron@192.168.1.6 'docker pull 192.168.1.6:5555/alert-crawler-news-post:v1.0.'$1
ssh noron@192.168.1.6 'docker rm -f alert-tc2-crawler-news-post'
ssh noron@192.168.1.6 'docker run --name alert-tc2-crawler-news-post -v /etc/hosts:/etc/hosts:ro -d --restart unless-stopped 192.168.1.6:5555/alert-crawler-news-post:v1.0.'$1
