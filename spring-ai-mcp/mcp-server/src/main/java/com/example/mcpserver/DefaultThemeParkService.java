package com.example.mcpserver;

import com.example.mcpserver.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultThemeParkService implements ThemeParkService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public DefaultThemeParkService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api.themeparks.wiki/v1").build();
        this.objectMapper = new ObjectMapper();
    }

    public String getParks() {
        var destinations = restClient
                .get()
                .uri("/destinations")
                .retrieve()
                .body(Destinations.class);
        
        List<Destination> destinationsList = List.of(destinations.destinations());
        List<Park> parks = new ArrayList<>();
        destinationsList.forEach(destination -> {
            for (DestinationPark park : destination.parks()) {
                parks.add(new Park(park.id(), park.name(), destination.id(), destination.name()));
            }
        });

        try {
            return objectMapper.writeValueAsString(parks);
        } catch(Exception e) {
            return "[]";
        }
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
        var liveEntity = restClient
                .get()
                .uri("/entity/{entityId}/live", entityId)
                .retrieve()
                .body(LiveEntity.class);

        try {
            return objectMapper.writeValueAsString(liveEntity);
        } catch(Exception e) {
            return "{}";
        }
    }

    @Override
    public String getEntityChildren(String entityId) {
        return restClient
                .get()
                .uri("/entity/{entityId}/children", entityId)
                .retrieve()
                .body(String.class);
    }

}
