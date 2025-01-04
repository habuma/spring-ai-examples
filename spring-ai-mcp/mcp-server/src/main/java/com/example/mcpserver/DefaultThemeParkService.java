package com.example.mcpserver;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DefaultThemeParkService implements ThemeParkService {

    private final RestClient restClient;

    public DefaultThemeParkService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api.themeparks.wiki/v1").build();
    }

    @Override
    public String getDestinations() {
        return restClient
                .get()
                .uri("/destinations")
                .retrieve()
                .body(String.class);
    }

    @Override
    public String getEntitySchedule(String entityId) {
        return restClient
                .get()
                .uri("/entity/{entityId}/schedule", entityId)
                .retrieve()
                .body(String.class);
    }

    @Override
    public String getEntityLive(String entityId) {
        return restClient
                .get()
                .uri("/entity/{entityId}/live", entityId)
                .retrieve()
                .body(String.class);
    }
}
