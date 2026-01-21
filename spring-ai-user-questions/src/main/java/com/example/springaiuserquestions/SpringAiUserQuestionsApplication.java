package com.example.springaiuserquestions;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiUserQuestionsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAiUserQuestionsApplication.class, args);
  }

  @Bean
  ApplicationRunner go(ChatClient chatClient) {
    return args -> {
      var response = chatClient.prompt()
          .user("What board game should we play?")
          .call()
          .content();

      System.out.println(response);
    };
  }

}
