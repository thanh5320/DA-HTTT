package com.hust.movie_review.es_repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
@Configuration
//@ComponentScan(basePackages = {"com.alert.elasticsearch"})
public class Config {
//    @Value("${elasticsearch.url}")
//    private String elasticsearchUrl;

    @Bean
    public RestHighLevelClient client() {
        RestHighLevelClient client = null;
        try {
            List<String> hosts = asList("38.242.198.251:9200");
            List<HttpHost> listHttpHost = new LinkedList<>();

            for (String host : hosts) {
                String addr = host.split(":")[0];
                int port = Integer.parseInt(host.split(":")[1]);
                listHttpHost.add(new HttpHost(addr, port, "http"));
            }

            HttpHost[] httpHosts = listHttpHost.stream().toArray(h -> new HttpHost[h]);

            RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);

            client = new RestHighLevelClient(restClientBuilder);
            log.info("Initialize connection at addr: {}", hosts);
        } catch (Exception e) {
            log.error("Fail to create ES RestHighLevelClient: message = {}, error ={}", e.getMessage(), e);
            System.exit(1);
        }
        return client;
    }
}