#!/bin/bash
/home/thanhnv/Tools/ELK/elasticsearch/elasticsearch-7.15.2/bin/elasticsearch -d
/home/thanhnv/Tools/ELK/kibana/kibana-7.15.2-linux-x86_64/bin/kibana  &
/home/thanhnv/Tools/Zookeeper/apache-zookeeper-3.8.0-bin/bin/zkServer.sh  start
/home/thanhnv/Tools/Kafka/kafka-3.2.0-src/bin/kafka-server-start.sh -daemon  /home/thanhnv/Tools/Kafka/kafka-3.2.0-src/config/server.properties
/home/thanhnv/Tools/ELK/logstash/logstash-7.15.2/bin/logstash -f /home/thanhnv/Tools/ELK/logstash/logstash-7.15.2/config/logstash.conf
