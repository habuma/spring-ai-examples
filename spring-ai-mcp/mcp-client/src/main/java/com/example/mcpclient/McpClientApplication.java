package com.example.mcpclient;

import org.springframework.ai.mcp.client.McpClient;
import org.springframework.ai.mcp.client.McpSyncClient;
import org.springframework.ai.mcp.client.transport.ServerParameters;
import org.springframework.ai.mcp.client.transport.SseClientTransport;
import org.springframework.ai.mcp.client.transport.StdioClientTransport;
import org.springframework.ai.mcp.spec.ClientMcpTransport;
import org.springframework.ai.mcp.spring.McpFunctionCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class McpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class, args);
    }

    @Bean(initMethod = "initialize", destroyMethod = "close")
    public McpSyncClient mcpClient(ClientMcpTransport transport) {
        return McpClient.using(transport).sync();
    }

    @Bean
    @Profile("!sseTransport")
    public ClientMcpTransport stdioTransport() {
        return new StdioClientTransport(ServerParameters.builder("java")
                .args("-jar", "../mcp-server/build/libs/mcp-server-0.0.1-SNAPSHOT.jar")
                .build());
    }

    @Bean
    @Profile("sseTransport")
    public ClientMcpTransport sseTransport() {
        return new SseClientTransport(WebClient.builder()
                .baseUrl("http://localhost:9090"));
    }

    @Bean
    public McpFunctionCallback[] functionCallbacks(McpSyncClient mcpClient) {
        return mcpClient.listTools(null)
                .tools()
                .stream()
                .map(tool -> new McpFunctionCallback(mcpClient, tool))
                .toArray(McpFunctionCallback[]::new);
    }

}
