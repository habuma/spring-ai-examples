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
    var media = new Media(
        MimeTypeUtils.IMAGE_JPEG,
        forecastImageResource.getContentAsByteArray());

    var userMessage = new UserMessage(question.question(), List.of(media));

    var response = chatClient.call(new Prompt(List.of(userMessage)));

    return new Answer(response.getResult().getOutput().getContent());
  }

}
