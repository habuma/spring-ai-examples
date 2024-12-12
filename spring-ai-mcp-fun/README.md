Spring AI MCP Fun
===
This project demonstrates how to use Spring AI with [Model Context Protocol
(MCP)](https://modelcontextprotocol.io/). It is a slight twist of the
[SQLite Spring AI MCP example](https://github.com/spring-projects/spring-ai-examples/tree/main/model-context-protocol/sqlite/simple), but makes use of the Google Maps MCP Server instead of the SQLite MCP
Server.

Before running the application, you'll need...

 - ...to have `npx` installed. The Google Maps MCP Server is Node.js based and the
   application will try to start it using `npx`.
 - ...to have set the `GOOGLE_MAPS_API_KEY` environment variable to a valid
   API key suitable for consuming the Google Maps API.
 - ...to have set the `OPENAI_API_KEY` environment variable to a valid OpenAI
   API key.

With those prerequisites in place, run the application using the Gradle Spring
Boot plugin like this:

```bash
$ ./gradlew bootRun
```

If all goes well, the application will start, in turn starting the Google Maps
MCP Server, and then pose a location-based question to the LLM. The question and
(after a few moments) the answer will be emitted to the console.

## What's happening?

Here's what's happening under the covers:

When the `mcpClient` bean is initialized, it fetches a list of available tools
from the MCP server. That list of tools is ultimately converted to a list of
function callbacks that are set as the default functions when creating the
`ChatClient` bean.

When the question is submitted through the `ChatClient`, the prompt sent to
the LLM will include the list of tools. The LLM will determine that one or more
of those tools need to be called and will respond back to the application with
instructions on how to call those tools.

The application will call those tools to get the data that the LLM needs to
answer the question. (The tool calls are all handled by Spring AI, so you don't
actually see that in the application code.) Spring AI will then send the prompt
back to the LLM with the tool responses. If the LLM needs more information, it
will respond with more tools request instructions. Otherwise, it will respond
with the answer to the question.

In essence, this mechanism is nothing new. Spring AI has supported tools calls
for a long while. What's new, however, is that the tools are not defined in the
application itself and instead are provided by the MCP server.
