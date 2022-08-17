#!/bin/bash
mvn clean install -DskipTests
docker build -t httt-backend:v1.0.1 .
