# Spring AI Examples

This repository is where I'll commit various examples of using Spring AI.

You can clone this project in its entirety and work with it like that. *Or*
better, use the [SpringCLI](https://docs.spring.io/spring-cli/reference/index.html)
to select individual projects and create them locally.

## Working With a Single Example

Each directory in this repository is a self-contained sample with its own build
wrapper and README.

For Maven-based examples, run the wrapper from the example directory:

```shell
cd spring-ai-rag-example
./mvnw spring-boot:run
```

For Gradle-based examples, use the local Gradle wrapper:

```shell
cd spring-ai-a2a
./gradlew bootRun
```

For the JBang scripts, run them directly from the `jbang-mcp` directory:

```shell
cd jbang-mcp
jbang McpClientStdio.java
```

Before starting an example, check its README for any required environment
variables or local dependencies such as Docker, Ollama, or JBang.

## Want more?

If you like this repository of example, then you're going to love [Spring AI in
Action](https://www.manning.com/books/spring-ai-in-action?a_aid=habuma&a_bid=f205d999&chan=habuma),
now available at Manning.com, Amazon, and anywhere you buy tech books. It covers all aspects of
working with Spring AI with a fun example that runs throughout most of the book.

![Spring AI in Action](https://www.habuma.com/img/SAIiA_small.png "Spring AI in Action")
