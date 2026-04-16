JBang + Spring AI MCP Examples
===

The code in this folder demonstrate how to use [JBang](https://www.jbang.dev/)
with [Spring AI](https://spring.io/projects/spring-ai) to create MCP servers
and clients.

There are two MCP servers, one each for the STDIO transport and the HTTP
Streaming transport. And there are corresponding clients for each server.

Prerequisites
---
Before running the scripts, make sure these commands work in your shell:

```shell
jbang version
ollama list
ollama pull gpt-oss
```

To use these scripts, you'll need to first make sure that JBang is installed.
And if you will be using the clients, you'll need to have
[Ollama](https://ollama.com/) installed and will need to have pulled the
GPT-OSS model. If you only want to run the servers, JBang alone is sufficient.

STDIO
---
When using the STDIO transport, the client is responsible for starting the
server in a process. The communication between client and server takes place
over standard input/output (STDIO).

One such client is the script in `McpClientStdio.java`. To run it using
JBang:

```shell
jbang McpClientStdio.java
```

When the client starts up, it will also start the server defined in
`McpServerStdio.java`. It will submit a prompt to the GPT-OSS model in Ollama
asking about the weather in Denver. After consulting with the "get-weather"
tool provided by the MCP Server, it should report that it is rainy and 79F.

Alternatively, you can configure any other MCP client, including
[Goose](https://block.github.io/goose/), the
[MCP Inspector](https://modelcontextprotocol.io/docs/tools/inspector) and
[MCP Jam](https://www.mcpjam.com/). to work with the server defined in
`McpServerStdio.java` by specifing `jbang` as the command and the path to
`McpServerStdio.java` as the arguments. Expressed in Claude Desktop
configuration, it looks like this:

```json
"mcpServers": {
  "jbang-weather": {
    "command": "/Users/habuma/.sdkman/candidates/jbang/current/bin/jbang",
    "args": [
      "/Users/habuma/Projects/spring-ai-examples/jbang-mcp/McpServerStdio.java"
    ]
  }
},
```

Or, as one more example, to configure this MCP server for use in Claude Code,
use the following command:

```shell
claude mcp add jbang-weather -- /path/to/jbang /path/to/McpServerStdio.java
```

Then, in your client of choice, ask about the weather in any city.

Streamable HTTP
---
For the Streamable HTTP transport, the server and client are run separately.

First, start the server:

```shell
jbang McpServerStreamableHttp.java
```

This should start the server, listening port 3001. The URI for the server is
http://localhost:3001/mcp.

In a separate terminal window, run the client:

```shell
jbang McpClientStreamableHttp.java
```

If everything goes well, after a short while you should be shown the weather
conditions in Denver: Rainy and 79F.

Of course, you can also use this MCP server using any client that supports the
Streamable HTTP transport, including [Goose](https://block.github.io/goose/),
the [MCP Inspector](https://modelcontextprotocol.io/docs/tools/inspector) and
[MCP Jam](https://www.mcpjam.com/).

Unfortunately, Claude Code does not support Streamable HTTP, but you can use a
proxy if you have `npx` installed:

```json
{
  "mcpServers": {
    "jbang-weather": {
      "command": "npx",
      "args": [
        "-y",
        "mcp-remote",
        "http://localhost:3001/mcp"
      ]
    }
  }
}
```

For one more example, here's how you would add the server in Claude Code:

```shell
claude mcp add --transport http jbang-weather http://localhost:3001/mcp
```
