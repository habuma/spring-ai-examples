package com.example.springaimultimodal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

  private final ChatClient chatClient;

  @Value("classpath:/forecast.jpg")
  Resource forecastImageResource;

  public AskController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @PostMapping("/ask")
  public Answer ask(@RequestBody Question question) throws Exception {
    var answer = chatClient.prompt()
        .user(userSpec -> userSpec
            .text(question.question())
            .media(MimeTypeUtils.IMAGE_JPEG, forecastImageResource))
        .call()
        .content();

    return new Answer(answer);
  }

}
