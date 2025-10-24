package habuma.springaitools;

import org.slf4j.Logger;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class WaitTimeService {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(WaitTimeService.class);

    private static final Map<String, String> ENTITY_ID_MAP = Map.of(
            "Jungle Cruise", "1b83fda8-d60e-48e4-9a3d-90ddcbcd1001",
            "Big Thunder Mountain", "0de1413a-73ee-46cf-af2e-c491cc7c7d3b",
            "Space Mountain", "9167db1d-e5e7-46da-a07f-ae30a87bc4c4"
    );

    @Tool(
        name = "getWaitTime",
        description = "Get the wait time for a Disneyland attraction in minutes"
    )
    public Response getWaitTime(@ToolParam(description = "The attraction's name") String attractionName) {
      LOGGER.info("Getting wait time for ride: " + attractionName);

      String entityId = ENTITY_ID_MAP.get(attractionName);
      if (entityId == null) {
        return new Response("Unknown");
      }
      LiveData liveData = RestClient.builder()
          .baseUrl("https://api.themeparks.wiki/v1/entity")
          .build()
          .get()
          .uri("/{entityId}/live", entityId)
          .retrieve()
          .body(LiveData.class);
      String waitTime = liveData.liveData().get(0).queue().STANDBY().waitTime();
      waitTime = waitTime != null ? waitTime : "unknown";
      return new Response(waitTime);
    }

    public record Response(String waitTime) {}

    public record LiveData(List<LiveDataItem> liveData) {}
    public record LiveDataItem(Queue queue) {}
    public record Queue(Standby STANDBY) {}
    public record Standby(String waitTime) {}

}
