package com.example.springaiqaadvisor;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.util.List;

@SpringBootApplication
public class SpringAiEvaluatorsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAiEvaluatorsApplication.class, args);
  }

  @Value("classpath:/BurgerBattle-rules.pdf")
  Resource documentResource;

  @Bean
  ApplicationRunner go(VectorStore vectorStore) {
    return args -> {

      TikaDocumentReader reader = new TikaDocumentReader(documentResource);
      List<Document> documents = reader.get();
      vectorStore.add(documents);


    };
  }

}
