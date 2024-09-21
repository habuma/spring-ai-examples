package com.example.springairag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ask")
public class AskController {

    private final ChatClient aiClient;
    private final VectorStore vectorStore;

    public AskController(ChatClient aiClient, VectorStore vectorStore) {
        this.aiClient = aiClient;
        this.vectorStore = vectorStore;
    }

    @PostMapping
    public Answer ask(@RequestBody Question question) {
        String answer = aiClient.prompt()
            .user(question.question())
            .advisors(new QuestionAnswerAdvisor(vectorStore))
            .call()
            .content();

        return new Answer(answer);
    }

}
