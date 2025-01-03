package com.example.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.spring.McpFunctionCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
public class AskController {

    @Value("classpath:/systemPrompt.st")
    private Resource systemPromptTemplate;

    private final ChatClient chatClient;

    public AskController(ChatClient.Builder chatClientBuilder, McpFunctionCallback[] functionCallbacks) {
        this.chatClient = chatClientBuilder
                .defaultFunctions(functionCallbacks)
                .build();
    }

    @PostMapping("/ask")
    public Answer ask(@RequestBody Question question) {

        Instant now = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
                .withZone(ZoneId.systemDefault());
        String formattedNow = formatter.format(now);

        return chatClient.prompt()
                .system(systemSpec ->
                        systemSpec
                                .text(systemPromptTemplate)
                                .param("todaysDate", formattedNow))
                .user(question.question())
                .call()
                .entity(Answer.class);
    }

}
