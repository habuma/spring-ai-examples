package com.example.springaiuserquestions;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiTodoWriteToolApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAiTodoWriteToolApplication.class, args);
  }

  @Bean
  ApplicationRunner go(ChatClient chatClient) {
    return args -> {
      var response = chatClient.prompt()
          .user("Create a list of all of the resort hotels in Disney World, group them by price tier (value, moderate, deluxe, DVC), and print each resort's name in all uppercase.")
          .call()
          .content();

      System.out.println(response);
    };
  }

}
