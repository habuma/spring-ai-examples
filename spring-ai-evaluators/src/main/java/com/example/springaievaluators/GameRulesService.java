package com.example.springaievaluators;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class GameRulesService {

  private final ChatClient chatClient;

  public GameRulesService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
    this.chatClient = chatClientBuilder
        .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build())
        .build();
  }
  
  public ChatResponse askQuestion(String question) {
    return chatClient
        .prompt()
        .user(question)
        .call()
        .chatResponse();
  }
  
}
