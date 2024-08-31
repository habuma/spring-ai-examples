package com.example.springairag;

import java.io.File;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class SpringAIRAGExample {

    public static void main(String[] args) {
        SpringApplication.run(SpringAIRAGExample.class, args);
    }

    @Value("${app.resource}")
    private Resource documentResource;

    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    ApplicationRunner applicationRunner(VectorStore vectorStore) {
        return args -> {
            TikaDocumentReader documentReader = new TikaDocumentReader(documentResource);
            TextSplitter textSplitter = new TokenTextSplitter();
            vectorStore.accept(
                textSplitter.apply(
                    documentReader.get()));
        };
    }

}
