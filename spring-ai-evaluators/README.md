Spring AI Evaluators (with QuestionAnswerAdvisor)
===
This project is a variation of the spring-ai-qaadvisor example in which the
use of `ChatClient` and `QuestionAnswerAdvisor` has been extracted from the
`AskController` into a `GameRulesService`.

But the main point of this project is to demonstrate how to evaluate the
result from `GameRulesService` using Spring AI's `RelevancyEvaluator`. It
also makes use of Spring AI's support for Testcontainers to fire up a Chroma
database for use during the course of the test.

The evaluator test is found in 
`src/test/java/com/example/springaievaluators/evaluator/EvaluatorTests.java`.
To run the test with Maven, make sure you have Docker running and then use the 
following command:

```bash
$ ./mvnw test
```

The test should pass because the `GameRulesService` uses `QuestionAnswerAdvisor`
created with a `VectorStore` that should be loaded rules for the game being
asked about.

Of course, because this is also a complete Spring AI application, you can also
run the application and ask questions the same as for the spring-ai-qaadvisor
example:

```bash
$ ./mvnw spring-boot:run
```

Once the application has started, ask a question about the game (using HTTPie):

```bash
$ http :8080/ask question="What is the Grave Digger card?"
```

Or with curl:

```bash
$ curl http://localhost:8080/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is the Grave Digger card?"}'
```

