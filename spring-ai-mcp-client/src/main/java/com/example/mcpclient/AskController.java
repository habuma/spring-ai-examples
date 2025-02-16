package com.example.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

  private final ChatClient chatClient;
  private final ToolCallbackProvider tools;

  public AskController(ChatClient.Builder chatClientBuilder,
                       ToolCallbackProvider tools) { // inject ToolCallbackProvider
    this.chatClient = chatClientBuilder.build();
    this.tools = tools;
  }

  @PostMapping("/ask")
  public Answer ask(@RequestBody Question question) {
    return chatClient.prompt()
        .user(question.question())
        .tools(tools)  // configure tools
        .call()
        .entity(Answer.class);
  }

}
