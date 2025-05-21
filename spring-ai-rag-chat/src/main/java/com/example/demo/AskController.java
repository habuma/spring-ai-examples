package com.example.demo;

import static org.springframework.ai.chat.memory
    .ChatMemory.CONVERSATION_ID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

  private final ChatClient chatClient;

  public AskController(ChatClient.Builder chatClientBuilder,
                       VectorStore vectorStore) {
    ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
    this.chatClient = chatClientBuilder
        .defaultAdvisors(
            MessageChatMemoryAdvisor.builder(chatMemory).build(),
            QuestionAnswerAdvisor.builder(vectorStore).build(),
            SimpleLoggerAdvisor.builder().build())
        .build();
  }

  @PostMapping("/ask")
  public Answer ask(
      @RequestBody Question question,
      @RequestHeader(name="X_CONV_ID", defaultValue="defaultConversation") String conversationId) {

    return chatClient.prompt()
        .user(question.question())
        .advisors(spec -> spec.param(CONVERSATION_ID, conversationId))
        .call()
        .entity(Answer.class);

  }

}
