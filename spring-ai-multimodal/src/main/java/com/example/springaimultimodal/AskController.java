package com.example.springaimultimodal;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AskController {

  private final ChatClient chatClient;

  @Value("classpath:/forecast.jpg")
  Resource forecastImageResource;

  public AskController(ChatClient chatClient) {
    this.chatClient = chatClient;
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
