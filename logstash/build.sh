#!/bin/bash
./gradlew gem
docker build -t 192.168.1.6:5555/thanh-demo:v1.0.$1 ./news-post-filter/.
docker push 192.168.1.6:5555/thanh-demo:v1.0.$1