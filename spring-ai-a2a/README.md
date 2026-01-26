Spring AI A2A Demo
===
This example demonstrates how to create a very simple agent with Spring AI
and expose it via Agent-to-Agent Protocol (A2A).

This example builds against the Spring AI A2A Community project at
https://github.com/spring-ai-community/spring-ai-a2a. There is not (yet) a
build for that project available in any public repository. Therefore, you'll
need to build that project, installing it in your local Maven repository
before building and running this project.

You'll also need to obtain an OpenAI API key and set it to the `OPENAI_API_KEY`
environment variable. Then you can run the application using the Spring Boot
Gradle plugin:

```shell
./gradlew bootRun
```

After the application has started, you should be able to view the agent card
at http://localhost:8080/a2a/.well-known/agent-card.json:

```shell
curl localhost:8080/a2a/.well-known/agent-card.json
```

You can also access the agent using the [A2A Inspector](https://github.com/a2aproject/a2a-inspector).
Follow the instructions there to build and run the A2A Inspector. Then, once
the inspector is open in your browser, enter the URL for the agent card into
the text box at the top of the page and click the "Connect" button. The agent
card JSON should be displayed in the text box below the "Connect" button.

Then scroll down to the bottom of the page and type in a question to interact
with the agent. Try one of these questions:

- What time is it?
- What is the wait time for Pirates of the Caribbean?
- What is the weather in Denver?

The response should appear in the chat area above.
