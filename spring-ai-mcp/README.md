Spring AI MCP Server and Client
===

This example shows how to create an MCP server and client with Spring AI.

The MCP server exposes 3 tools that leverage the [Theme Park API](https://themeparks.wiki/api), including:

 - **destinations** : Returns the JSON from the `/destinations` endpoint. There is
 some initial effort to filter the JSON by a specific theme park, but that part
 is unfinished at this time. This tool effectively makes it possible to lookup a
 park's entity ID given its name.
 - **entity-schedule** : Returns JSON from the `/entity/{entity ID}/schedule`
 endpoint. Primarily used to retrieve operating hours for a park.
 - **entity-live** : Returns JSON from the `/entity/{entity ID}/live` endpoint.
 Can be used to retrieve wait times for an attraction.

What's important to note is that nowhere in the server or client is there any
code that instructs the LLM on when/if it should use one of those tools. The
tools are just made available and the LLM can decide to use them as it sees
fit when answering a question.

For example, if you were to ask "What time does Epcot close today?" the prompt
including that question as well as the tool definitions will be sent to the LLM.
The LLM will likely decide first to invoke the "destinations" tool to lookup the
entity ID for "Epcot" and then decide to invoke the "entity-schedule" tool to
get the operating hours. But it's the LLM making those decisions, not the code
of the server or client.

I intend to flesh this out further, adding more tools and improving the tools
that are there now. For instance:

 - I plan to add caching to the "destinations" tool as it is generally
 unnecessary to fetch that for every single question.
 - I plan to filter the JSON from the "destinations" tool to focus on the
 park or resort being asked about so that the entire JSON isn't sent in the
 prompt.

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
HTTPie, you ask questions like this:

```
$ http :8080/ask question="What time does Epcot close today?"
```

Or, perhaps this:

```
$ http :8080/ask question="What is the wait time for Indiana Jones in Disneyland?"
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
HTTPie, you ask questions like this:

```
$ http :8080/ask question="What time does Epcot close today?"
```

Or, perhaps this:

```
$ http :8080/ask question="What is the wait time for Indiana Jones in Disneyland?"
```
