package com.example.springaiuserquestions;

import org.springaicommunity.agent.tools.TodoWriteTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

  @Autowired
  ApplicationEventPublisher publisher;

  @Bean
  ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    return chatClientBuilder
        .defaultAdvisors(
            ToolCallAdvisor.builder().conversationHistoryEnabled(false).build(),
            MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
        .defaultTools(TodoWriteTool.builder()
            .todoEventHandler(event -> {
              System.err.println(event);
              var todos = event.todos();
              for (var todo: todos) {
                System.err.println(" -- " + todo);
              }
              new TodoUpdateEvent(this, event.todos());
            })
            .build())
        .build();
  }

}
