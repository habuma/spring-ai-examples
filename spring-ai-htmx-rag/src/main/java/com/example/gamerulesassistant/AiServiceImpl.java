package com.example.gamerulesassistant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

  private final ChatClient chatClient;

  private String documentName;

  public AiServiceImpl(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
    this.chatClient = chatClientBuilder
        .defaultAdvisors(
          QuestionAnswerAdvisor.builder(vectorStore).build(),
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

  @Override
  public String getDocumentName() {
    if (this.documentName != null) {
      return this.documentName;
    }

    this.documentName = chatClient.prompt()
        .user("What is the name of the game. Return only the name of the game as the answer.")
        .call()
        .content();

    return this.documentName;
  }

}
