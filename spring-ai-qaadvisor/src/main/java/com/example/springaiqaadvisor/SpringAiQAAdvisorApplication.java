package com.example.springaiqaadvisor;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class SpringAiQAAdvisorApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringAiQAAdvisorApplication.class, args);
  }

  @Value("classpath:/BurgerBattle-rules.pdf")
  Resource documentResource;

  @Bean
  ApplicationRunner go(VectorStore vectorStore) {
    return args -> {
      TikaDocumentReader reader = new TikaDocumentReader(documentResource);
      TokenTextSplitter splitter = new TokenTextSplitter();
      vectorStore.add(splitter.apply(reader.get()));
    };
  }

}
