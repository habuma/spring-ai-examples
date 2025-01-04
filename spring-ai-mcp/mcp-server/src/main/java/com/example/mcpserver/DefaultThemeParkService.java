package com.example.mcpserver;

import com.example.mcpserver.domain.Destination;
import com.example.mcpserver.domain.DestinationPark;
import com.example.mcpserver.domain.Destinations;
import com.example.mcpserver.domain.Park;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultThemeParkService implements ThemeParkService {

    private final RestClient restClient;

    public DefaultThemeParkService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api.themeparks.wiki/v1").build();
    }

    @Cacheable("parks")
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

        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(parks);
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
        return restClient
                .get()
                .uri("/entity/{entityId}/live", entityId)
                .retrieve()
                .body(String.class);
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
