package com.elasticsearch.elasticdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.elasticsearch.elasticdemo.repository")
@ComponentScan(basePackages = "com.elasticsearch.elasticdemo")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Value("${elasticsearch.url}")
    private String url;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(url)
                .build();
    }
}
