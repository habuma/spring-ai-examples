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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class SpringAIRAGExample {

    public static void main(String[] args) {
        SpringApplication.run(SpringAIRAGExample.class, args);
    }

    @Value("${app.vectorstore.path:/tmp/vectorstore.json}")
    private String vectorStorePath;

    @Value("${app.resource}")
    private Resource pdfResource;

    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder.build();
    }

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingModel);
        File vectorStoreFile = new File(vectorStorePath);
        if (vectorStoreFile.exists()) { // load existing vector store if exists
            simpleVectorStore.load(vectorStoreFile);
        } else { // otherwise load the documents and save the vector store
            TikaDocumentReader documentReader = new TikaDocumentReader(pdfResource);
            List<Document> documents = documentReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        }
        return simpleVectorStore;
    }

}
