package com.example.springaimcpfun;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.client.McpClient;
import org.springframework.ai.mcp.client.McpSyncClient;
import org.springframework.ai.mcp.client.stdio.ServerParameters;
import org.springframework.ai.mcp.client.stdio.StdioServerTransport;
import org.springframework.ai.mcp.spring.McpFunctionCallback;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.List;

@SpringBootApplication
public class SpringAiMcpFunApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiMcpFunApplication.class, args);
    }

    /*
      ApplicationRunner that asks a question that can only be answered
      with help from the Google Maps MCP Server.
     */
    @Bean
    public ApplicationRunner go(ChatClient chatClient) {
        return args -> {
            String question = "What is the home improvement store nearest a Sonic Drive-In in Castle Rock, CO?";
            System.out.println("QUESTION: " + question);
            System.out.println("ASSISTANT: " + chatClient.prompt(question).call().content());
        };
    }

    /*
      Declare the ChatClient bean.
      This is the main bean that will be used to interact with the AI.
      It is created with a builder that takes the list of default functions
      that are the tools supported by the MCP server.
     */
    @Bean
    ChatClient chatClient(ChatClient.Builder chatClientBuilder, List<McpFunctionCallback> functionCallbacks) {
        return chatClientBuilder
                .defaultFunctions(functionCallbacks.toArray(new McpFunctionCallback[0]))
                .build();
    }

    /*
      Declare a bean that is the list of MCP function callbacks.
      This essentially fetches the available tools from the MCP server and uses them
      to create a list of function callbacks that will be given as default functions
      when creating the ChatClient.
     */
    @Bean
    public List<McpFunctionCallback> functionCallbacks(McpSyncClient mcpClient) {
        var callbacks = mcpClient.listTools(null)
                .tools()
                .stream()
                .map(tool -> new McpFunctionCallback(mcpClient, tool))
                .toList();
        return callbacks;
    }

    /*
      Declare the MCP Client bean.
      This one starts the Google Maps MCP Server (which requires npx to be installed
      and for GOOGLE_MAPS_API_KEY to be set to a valid key for accessing the Google
      Maps API).
     */
    @Bean(destroyMethod = "close")
    public McpSyncClient mcpClient() {
        var stdioParams = ServerParameters.builder("npx")
                .args("-y", "@modelcontextprotocol/server-google-maps")
                .build();

        var mcpClient = McpClient.sync(new StdioServerTransport(stdioParams),
                Duration.ofSeconds(10), new ObjectMapper());

        var init = mcpClient.initialize();
        System.out.println("MCP Initialized: " + init);
        return mcpClient;
    }
}
