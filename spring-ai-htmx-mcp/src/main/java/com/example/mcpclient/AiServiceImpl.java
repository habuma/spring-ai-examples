package com.example.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

  private final ChatClient chatClient;

  public AiServiceImpl(ChatClient.Builder chatClientBuilder, ToolCallbackProvider toolCallbackProvider) {
    this.chatClient = chatClientBuilder
        .defaultTools(toolCallbackProvider)
        .defaultAdvisors(
            new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
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
