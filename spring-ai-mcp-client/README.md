# MCP Client

This is a simple MCP Client built with Spring AI.

## Configuring the Client

The application is configured in `src/main/resources/application.yml` to
load client configuration from `src/main/resources/mcp-servers.json`.
That JSON file follows the [Claude Desktop configuration format](https://modelcontextprotocol.io/quickstart/user).

The `mcp-servers.json` file is currently configured with STDIO connection
details for the [ThemeParks.wiki API MCP Server](https://github.com/habuma/tpapi-mcp-server),
assuming paths specific to my machine. You can change it to reference any
other MCP Server you want or, if you want to use it to connect to the
ThemeParks.wiki API MCP Server, then follow the README instructions for
the MCP Server and adjust the paths accordingly.

## Running the Client

Before running the application, you'll need to obtain an OpenAI API key from
https://platform.openai.com. Once you have an API key, set it to an environment
variable named `OPENAI_API_KEY`.

Run the client as you would any Spring Boot project, either by building
it and running the JAR file...

```
./gradlew build
java -jar build/libs/mcp-client-0.0.1-SNAPSHOT.jar
```

...or by using the Spring Boot build plugin:

```
./gradlew bootRun
```

### Asking Questions

Once the client is up and running, it should be listening on port 8080.
You can then ask questions by POSTing to the `/ask` endpoint with a JSON
payload the has a "question" property. For example, using `curl`:

```
curl -H "Content-Type: application/json" \
  http://localhost:8080/ask \
  -d '{"question":"What time does Epcot open tomorrow?"}'
```

Or by using [HTTPie](https://httpie.io/):

```
http :8080/ask question="What is the wait for Space Mountain in Disneyland?"
```

Note that in both the `curl` and HTTPie examples, the question asked is
one that the ThemeParks.wiki API MCP Server can help answer. If you're
using a different MCP Server, you'll need to ask questions that it can
help answer.
