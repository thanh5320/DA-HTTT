FROM docker.elastic.co/logstash/logstash:7.15.2
LABEL maintainer="thanh20183632@gmail.com"
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
COPY logstash-filter-news_post_filter-1.0.4.gem /usr/share/logstash
RUN /usr/share/logstash/bin/logstash-plugin install --no-verify --local /usr/share/logstash/logstash-filter-news_post_filter-1.0.4.gem
ADD logstash.conf /usr/share/logstash/pipeline
ADD logstash.yml /usr/share/logstash/config