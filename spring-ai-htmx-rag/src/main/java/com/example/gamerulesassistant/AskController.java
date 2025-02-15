package com.example.gamerulesassistant;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AskController {

  private final AiService aiService;

  public AskController(AiService aiService) {
    this.aiService = aiService;
  }


  @GetMapping("/")
  public String chat(Model model) {
    model.addAttribute("documentTitle", aiService.getDocumentName());
    return "index";
  }

  @PostMapping("/ask")
  public HtmxResponse chat(MessageIn messageIn, Model model) {
    String completion = aiService.complete(messageIn.message());
    model.addAttribute("messageIn", messageIn);
    model.addAttribute("messageOut", completion);
    return HtmxResponse.builder()
        .view("chatMessage :: chatFragment")
        .build();
  }

}
