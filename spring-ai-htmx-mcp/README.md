# Spring AI MCP Client

This is an example of using Spring AI as an MCP Client, similar to how [Claude
Desktop](https://claude.ai/download) works. It provides a simple browser based 
chat interface, implemented with [HTMX](https://htmx.org/).

Unlike Claude Desktop, this client does not use Anthropic's Claude models. Instead,
it is configured to use OpenAI's gpt-4o-mini model. You can change the model by
modifying the application.yml file, following the directions in the comments.

If you'd rather use an entirely different GenAI API (such as Ollama, Gemini, or
even Anthropic), you'll need to change the dependency in the build.gradle file
and set the appropriate configuration in the application.yml file.

## Pre-Requisite Setup

Before you can run the application, you'll need to obtain an API key from OpenAI
and set an environment variable named `OPENAI_API_KEY` to the value of your API key.

Then, you'll need to configure one or more MCP Servers in a configuration file
with the same format as Claude Desktop's configuration. For example, the following
configuration file would connect to the [ThemeParks.wiki MCP Server](https://github.com/habuma/tpapi-mcp-server).
(See the README in that repository for instructions on building the server.)

```yaml
{
  "mcpServers": {
    "tpapi": {
      "command": "/path/to/java",
      "args": [
        "-jar",
        "/path/to/project/build/libs/tpapi-mcp-server-0.0.1-SNAPSHOT.jar"
      ]
    }
  }
}
```

Wherever you create that configuration file, adjust the path to it in the 
`application.yml` file, as directed in the comments.

## Running the Application

Run the application as you would any Spring Boot application. The easiest way is 
perhaps  to take advantage of the Spring Boot Gradle plugin. From the root of the 
project, run:

```shell
./gradlew bootRun
```

Then open a browser and navigate to `http://localhost:8080` to interact with the 
chat interface.