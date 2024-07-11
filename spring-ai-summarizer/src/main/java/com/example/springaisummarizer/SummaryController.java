package com.example.springaisummarizer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SummaryController {

  private static final String SUMMARY_SYSTEM = """
      Summarize the content of the given text in the DOCUMENT entry.
      Summarize each section of the document separately as well as
      the entire document. The summary for each section should be no
      longer than 2 paragraphs. The summary for the entire document
      should be no longer than 1 paragraph.
      
      DOCUMENT:
      {document}
      """;

  private final ChatClient chatClient;

  public SummaryController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder
        .build();
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
            .text(SUMMARY_SYSTEM)
            .param("document", documentText))
        .call()
        .content();
  }

}
