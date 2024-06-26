Spring AI and RAG with Kotlin
===
This example shows how to do a RAG application in Kotlin with Spring
AI, using a Chroma vector store and Spring AI's `QuestionAnswerAdvisor`.

The document loaded for Q&A is the rules for the game
[Burger Battle](https://www.burgerbattlegame.com/) in
`src/main/resources/BurgerBattle-rules.pdf`.

Before running the application, you'll need to obtain an API key for
OpenAI's API. Go to https://openai.com/, sign up for an account, and
generate an API key. Once you have an API key, set it to the
`OPENAI_API_KEY` environment variable. For example:

~~~
% export OPENAI_API_KEY=sk-...
~~~

(The actual API key will be quite a bit longer.)

Also, the Chroma vector store is run in Docker using Spring Boot's
support for Docker Compose. You don't need to explicitly start Chroma
before running the application, but make sure Docker is running so 
that Spring Boot can start Chroma for you.

The easiest way to run the application is to use the Spring Boot Maven
plugin:

~~~
% ./mvnw spring-boot:run
~~~

Once the application starts up, you can begin asking questions by
POSTing to the `/ask` endpoint. The body of the POST request should be
a simple JSON document with a "question" property.

For example, here's how you might ask a question using the `curl`
command line tool:

~~~
% curl localhost:8080/ask \
  -H"Content-type: application/json" \
  -d'{"question":"What is the burgerpocalypse card?"}'
~~~

Or, if using HTTPie:

~~~
% http :8080/ask question="What is the burgerpocalypse card?"
~~~
