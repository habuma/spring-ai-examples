package com.example.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

  private final ChatClient chatClient;

  public AiServiceImpl(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
    this.chatClient = chatClientBuilder
        .defaultToolCallbacks(toolCallbackProvider)
        .defaultAdvisors(
          MessageChatMemoryAdvisor.builder(
            MessageWindowChatMemory.builder().build())
            .build())
        .build();
  }

  @Override
  public String complete(String message) {
    return chatClient.prompt()
        .user(message)
        .call()
        .content();
  }

}
