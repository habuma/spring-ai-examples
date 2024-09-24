package com.example.demo;

import org.slf4j.Logger;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class LoaderConfig {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(LoaderConfig.class);

  @Value("${app.resource}")
  Resource documentResource;

  @Bean
  ApplicationRunner applicationRunner(VectorStore vectorStore) {
    return args -> {
      TikaDocumentReader documentReader = new TikaDocumentReader(documentResource);
      TextSplitter textSplitter = new TokenTextSplitter();
      log.info("Loading document into vector store");
      vectorStore.accept(
          textSplitter.apply(
              documentReader.get()));
      log.info("Documents loaded into vector store");
    };
  }

}
