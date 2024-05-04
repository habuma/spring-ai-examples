package com.example.vectorstoreloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Component
public class VectorStoreLoaderConsumer implements Consumer<Flux<Message<byte[]>>> {

    private final Logger LOGGER = LoggerFactory.getLogger(VectorStoreLoaderConsumer.class);

    private final TextSplitter textSplitter = new TokenTextSplitter();
    private final VectorStore vectorStore;

    public VectorStoreLoaderConsumer(VectorStore vectorStore) {
      this.vectorStore = vectorStore;
    }

    @Override
    public void accept(Flux<Message<byte[]>> incomingMessage) {
      incomingMessage
        .doOnNext(message -> {
          MessageHeaders headers = message.getHeaders();
          String fileName = Objects.requireNonNull(headers.get("file_name")).toString();
          LOGGER.info("Picking up {} to add to the vector store.", fileName);
          ByteArrayResource resource = new ByteArrayResource(message.getPayload());
          TikaDocumentReader reader = new TikaDocumentReader(resource);
          List<Document> documents = textSplitter.apply(reader.get())
              .stream()
              .peek(document -> document.getMetadata().put("source", fileName))
              .toList();
          vectorStore.add(documents);
          LOGGER.info("Finished adding {} documents for {}.", documents.size(), fileName);
        }).subscribe();
    }

}
