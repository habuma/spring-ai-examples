Spring AI MCP Server and Client
===

This example shows how to create an MCP server and client with Spring AI.

At the moment, the MCP server recreates the same calculator/weather example
provided by the Spring AI itself. But the client goes a step further than
the Spring AI-provided example by integrating the client with `ChatClient`
so that you can ask questions that rely on the MCP server for answers.

(Note: I plan to rework the server at some point to do something different
than what the Spring AI-provided example does. I just started with this to
get something initial working.)

Running with the Stdio transport
---
At startup, the client will attempt to launch the server by running the
server's executable JAR file. Therefore, you will need to build the server
application before running the client:

```
$ cd mcp-server; ./gradlew build; cd ..
```

Again, the client application will run the server, so there's no need for
you to run the server yourself. Just build it to make sure that the JAR is
in place for the client to find.

Next, run the client:

```
$ cd mcp-client; ./gradlew bootRun
```

Once the client has started, you can send POST requests to the /ask endpoint
to ask questions that the server can help to answer. For example, using
HTTPie, you can exercise the server's calculator tool:

```
$ http :8080/ask question="What is 1.2 plus 3.4?"
```

Or, to exercise the server's weather tool:

```
$ http :8080/ask question="What is the weather in Boston?"
```

Running with the SSE transport
---
The client will communicate with the server via HTTP(S). Therefore, you
must run the server--with the "sseTransport" profile active--before running
the client:

```
$ cd mcp-server; ./gradlew bootRun --args='--spring.profiles.active=sseTransport'
```

With the server running, you can start the client (also with the
"sseTransport" profile active). In a separate console window:

```
$ cd mcp-client; ./gradlew bootRun --args='--spring.profiles.active=sseTransport'
```

Once the client has started, you can send POST requests to the /ask endpoint
to ask questions that the server can help to answer. For example, using
HTTPie, you can exercise the server's calculator tool:

```
$ http :8080/ask question="What is 1.2 plus 3.4?"
```

Or, to exercise the server's weather tool:

```
$ http :8080/ask question="What is the weather in Boston?"
```
