package com.example.springaia2a;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MyTools {

  @Tool(description = "Get the current weather for a given location")
  public String weather(
      @ToolParam(description = "The zipcode to get the weather for") String zipcode) {
    return "The current weather in " + zipcode + " is rainy with a temperature of 12°C.";
  }

  @Tool(description = "Gets the current date and time")
  public String getCurrentDateAndTime() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  @Tool(description = "Finds the current wait time for an attraction in Disneyland in minutes")
  public Integer getWaitTime(
      @ToolParam(description = "The attraction name") String attractionName) {
    return 20;
  }

}
