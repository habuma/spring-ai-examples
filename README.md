# Spring AI Examples

This repository is where I'll commit various examples of using Spring AI.

You can clone this project in its entirety and work with it like that. *Or*
better, use the [SpringCLI](https://docs.spring.io/spring-cli/reference/index.html)
to select individual projects and create them locally.

## Prerequisites

- JDK 17+
- Maven / Gradle (or use each sub-project's wrapper)
- Optional model provider key (for example DashScope/OpenAI compatible endpoint)

## Run a Single Example

Most examples are independent sub-projects. For example:

```bash
cd spring-ai-rag-chat
./mvnw test
./mvnw spring-boot:run
```

## Run All Example Tests

From repository root:

```bash
chmod +x build-all.sh
./build-all.sh
```

## Configuration Notes

- Put sensitive keys in environment variables, do not hardcode them in source files.
- Prefer creating local profile files (for example `application-local.yml`) and keep them out of git.

## Want more?

If you like this repository of example, then you're going to love [Spring AI in
Action](https://www.manning.com/books/spring-ai-in-action?a_aid=habuma&a_bid=f205d999&chan=habuma),
now available at Manning.com, Amazon, and anywhere you buy tech books. It covers all aspects of
working with Spring AI with a fun example that runs throughout most of the book.

![Spring AI in Action](https://www.habuma.com/img/SAIiA_small.png "Spring AI in Action")

## Contributor Quick Verification
<!-- codex-batch30-readme -->

Before opening a PR, run these quick checks locally:

- Build: `git status`
- Tests: `(no dedicated test command documented)`
- Keep changes focused: one topic per PR with a clear title

