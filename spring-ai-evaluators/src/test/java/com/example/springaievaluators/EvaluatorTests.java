package com.example.springaievaluators;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.qdrant.QdrantContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class EvaluatorTests {

  @Container
  @ServiceConnection
  static QdrantContainer chroma = new QdrantContainer("qdrant/qdrant");

  @Autowired
  ChatModel chatModel;

  @Autowired
  GameRulesService gameRulesService;

  @Test
  void testEvaluation() {
    String question = "What is the grave digger card?";
    ChatResponse response = gameRulesService.askQuestion(question);
    String responseContent = response.getResult().getOutput().getText();
    var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));

    @SuppressWarnings("unchecked")
    List<Document> responseDocuments =
        (List<Document>) response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);

    var evaluationRequest = new EvaluationRequest(question, responseDocuments, responseContent);

    EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

    assertTrue(evaluationResponse.isPass(),
        "Response is not relevant to the asked question.\n" +
            "Question: " + question + "\n" +
            "Response: " + responseContent);
  }

}
