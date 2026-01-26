package com.example.springaia2a;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentSkill;
import org.springaicommunity.a2a.server.executor.DefaultA2AChatClientAgentExecutor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

  @Bean
  public AgentCard agentCard(@Value("${server.port:8080}") int port) {
    return new AgentCard.Builder()
        .name("My Agent")
        .description("Provides a mix of my tools")
        .url("http://localhost:" + port + "/a2a/")
        .version("1.0.0")
        .capabilities(new AgentCapabilities.Builder().streaming(false).build())
        .defaultInputModes(List.of("text"))
        .defaultOutputModes(List.of("text"))
        .skills(List.of(new AgentSkill.Builder()
                .id("weather").name("Get Weather")
                .description("Answers questions regarding weather")
                .tags(List.of("weather")).build(),
            new AgentSkill.Builder()
                .id("waittime").name("Get Wait Times")
                .description("Answers questions regarding wait times for attractions in Disneyland")
                .tags(List.of("wait")).build(),
            new AgentSkill.Builder()
                .id("time").name("Get Current Time")
                .description("Answers questions regarding the current time")
                .tags(List.of("time")).build()))
        .protocolVersion("0.3.0")
        .build();
  }

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder, MyTools tools) {
    return chatClientBuilder
        .defaultTools(tools)
        .build();
  }

  @Bean
  public AgentExecutor agentExecutor(ChatClient chatClient) {
    return new DefaultA2AChatClientAgentExecutor(chatClient, (chat, ctx) -> {
      String userMessage = DefaultA2AChatClientAgentExecutor.extractTextFromMessage(ctx.getMessage());
      return chat.prompt().user(userMessage).call().content();
    });
  }

}
