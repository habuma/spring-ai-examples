package com.example.demo
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class Question(val question: String)
data class Answer(val answer: String?)

@RestController
class AskController(chatClientBuilder: ChatClient.Builder, vectorStore: VectorStore) {
    private val chatClient = chatClientBuilder
        .defaultAdvisors(
            QuestionAnswerAdvisor.builder(vectorStore).build())
        .build()

    @PostMapping("/ask")
    fun ask(@RequestBody question: Question) = Answer (
        chatClient
            .prompt()
            .user(question.question)
            .call()
            .content()
    )
}
