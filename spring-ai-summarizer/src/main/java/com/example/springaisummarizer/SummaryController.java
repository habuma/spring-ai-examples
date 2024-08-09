package com.example.springaisummarizer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SummaryController {

  @Value("classpath:/summarize.st")
  private Resource summarizeTemplate;

  private final ChatClient chatClient;

  public SummaryController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @PostMapping(path="/summarize", produces = "text/plain")
  public String summarize(@RequestParam("file")MultipartFile file) {
    Resource resource = file.getResource();
    List<Document> documents = new TikaDocumentReader(resource).get();
    String documentText = documents.stream()
        .map(Document::getContent)
        .collect(Collectors.joining("\n\n"));

    return chatClient.prompt()
        .system(systemSpec -> systemSpec
            .text(summarizeTemplate)
            .param("document", documentText))
        .call()
        .content();
  }

}
