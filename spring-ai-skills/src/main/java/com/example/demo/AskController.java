package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

  private final ChatClient chatClient;

  public AskController(ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @PostMapping("/ask")
  public Answer ask(@RequestBody Question question) {
    var response = chatClient.prompt()
        .user(question.question())
        .call()
        .content();
    return new Answer(response);
  }

}
