#!/bin/bash
mvn clean install -DskipTests
docker build -t httt-backend:v1.0.1 .
docker run --name httt-backend -v /etc/hosts:/etc/hosts:ro -d -p 8082:8082 --restart unless-stopped httt-backend:v1.0.1