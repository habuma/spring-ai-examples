package com.example.vectorstoreloader;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VectorStoreLoaderApplication {

  public static void main(String[] args) {
    SpringApplication.run(VectorStoreLoaderApplication.class, args);
  }

  @Bean
  ApplicationRunner go(FunctionCatalog catalog) {
    Runnable composedFunction = catalog.lookup(null);
    return args -> composedFunction.run();
  }

}
