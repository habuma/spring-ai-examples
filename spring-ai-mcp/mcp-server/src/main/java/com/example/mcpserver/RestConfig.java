package com.example.mcpserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

}
