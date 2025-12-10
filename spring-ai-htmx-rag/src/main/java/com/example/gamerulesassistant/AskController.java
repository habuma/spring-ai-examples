package com.example.gamerulesassistant;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.FragmentsRendering;

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
  public FragmentsRendering chat(MessageIn messageIn) {
    String completion = aiService.complete(messageIn.message());

    var model = Map.of(
      "messageIn", messageIn,
      "messageOut", completion);

      return FragmentsRendering
          .with("chatMessage", model)
          .build();
  }

}
