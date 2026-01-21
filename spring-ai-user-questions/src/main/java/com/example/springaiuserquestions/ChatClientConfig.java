package com.example.springaiuserquestions;

import org.springaicommunity.agent.tools.AskUserQuestionTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ChatClientConfig {

  @Bean
  ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    return chatClientBuilder
        .defaultTools(AskUserQuestionTool.builder()
            .questionHandler(this::handleQuestions)
            .build())
        .build();
  }

  // This question handler comes straight from Christian's Agentic AI Patterns blog, part 2.
  // It's quite useful as-is, so no need to reinvent it. Perhaps it should be offered as an
  // out-of-the-box handler for ease of use.
  private Map<String, String> handleQuestions(List<AskUserQuestionTool.Question> questions) {
    Map<String, String> answers = new HashMap<>();
    Scanner scanner = new Scanner(System.in);
    for (AskUserQuestionTool.Question q : questions) {
      System.out.println("\n" + q.header() + ": " + q.question());
      for (int i = 0; i < q.options().size(); i++) {
        AskUserQuestionTool.Question.Option opt = q.options().get(i);
        System.out.printf(" %d. %s - %s%n", i + 1, opt.label(), opt.description());
      }
      System.out.println(q.multiSelect()
          ? " (Enter numbers separated by commas, or type custom text)"
          : " (Enter a number, or type custom text)");
      String response = scanner.nextLine().trim();
// Parse numeric selection(s) or use as free text
      try {
        String[] parts = response.split(",");
        List<String> labels = new ArrayList<>();
        for (String part : parts) {
          int index = Integer.parseInt(part.trim()) - 1;
          if (index >= 0 && index < q.options().size()) {
            labels.add(q.options().get(index).label());
          }

        }
        answers.put(q.question(), labels.isEmpty() ? response : String.join(", ", labels));
      } catch (NumberFormatException e) {
        answers.put(q.question(), response);
      }
    }
    return answers;
  }

}
