Spring AI Skills Demo
===
This project demonstrates how to use Claude-like skills in Spring AI.
It demonstrates that Claude Skills can be defined and used in Spring AI
even when not using a Claude model.

Before running this demo, you'll need to decide whether you'll be testing
this with Anthropic, OpenAI, Ollama, or Gemini. The OpenAI dependency is
uncommented in build.gradle, but you can comment it out and uncomment one
of the other dependencies.

Then, unless you choose Ollama, you'll need to obtain an API KEY (or project
ID in the case of Gemini) and set the appropriate environment variable:

 - `OPENAI_API_KEY` - For OpenAI
 - `ANTHROPIC_API_KEY` - For Claude
 - `GOOGLE_CLOUD_PROJECT` - For Google

The skill used in this project executes a Python script using the [uv](https://github.com/astral-sh/uv)
package manager. For it to work, you'll need to have `uv` installed.

Then run the application as you would any Spring Boot application with the
Spring Boot Gradle plugin:

```shell
./gradlew bootRun
```

Once teh application has started, you can ask it questions via the `/ask`
endpoint exposed by the `AskController`. For example (using [HTTPie](https://httpie.io/)):

```shell
http :8080/ask question="Why is the sky blue?"
```

This should work, but doesn't exercise the skill that's included in the project
(under `src/main/resources/.claude/skills/joke`). To try that one out, ask
it to tell you a joke:

```shell
http :8080/ask question="Tell me a joke"
```

Without the skill in play, you would probably get one of the jokes commonly told
by an LLM, such as why scientists don't trust atoms or some such thing. But the
joke skill has a joke hardcoded to always return. And because the joke is not one
likely to be returned from the LLM, you can be sure that it came from the skill
and not from the LLM's training.

## How it works

In a nutshell:

 - The joke skill is made up of two parts--SKILL.md and scripts/joker.py.
 - SKILL.md is standard fare for a skill and describes what the skill does as well
 as how to do it. In this case, it says that the joker.py script should be invoked
 to tell a joke.
 - The joker.py script is a very simple Python script that prints a hard-coded
 joke. 
 - When the user asks to tell a joke, the skill's information is used to define a
 tool. The tool information is sent to the LLM and the LLM decides whether or not
 the tool should be invoked (in typical tool-calling fashion).
 - If the tool is invoked, then the output from the Python script is used as the
 return value and is what the LLM uses as the tool's response.
 - The `ShellTools` configured when creating the `ChatClient` enables shell
 commands (such as `uv`) to be invoked in the course of invoking a tool.
 - From there, it's as if you wrote the tool in Java as a `@Tool`-annotated method.

