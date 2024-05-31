package com.example.springaievaluators;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

  private final GameRulesService gameRulesService;

  public AskController(GameRulesService gameRulesService) {
    this.gameRulesService = gameRulesService;
  }

  @PostMapping("/ask")
  public Answer ask(@RequestBody Question question) {
    ChatResponse chatResponse = gameRulesService.askQuestion(question.question());
    return new Answer(chatResponse.getResult().getOutput().getContent());
  }

}
