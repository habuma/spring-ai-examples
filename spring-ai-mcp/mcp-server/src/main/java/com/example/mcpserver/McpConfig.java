package com.example.mcpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.mcp.server.McpAsyncServer;
import org.springframework.ai.mcp.server.McpServer;
import org.springframework.ai.mcp.server.transport.SseServerTransport;
import org.springframework.ai.mcp.server.transport.StdioServerTransport;
import org.springframework.ai.mcp.spec.McpSchema;
import org.springframework.ai.mcp.spec.McpTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;
import java.util.Map;

@Configuration
public class McpConfig {

    @Bean
    @Profile("!sseTransport")
    public StdioServerTransport stdioServerTransport() {
        return new StdioServerTransport();
    }

    @Bean
    @Profile("sseTransport")
    public SseServerTransport sseServerTransport() {
        return new SseServerTransport(new ObjectMapper(), "/mcp/message");
    }

    @Bean
    @Profile("sseTransport")
    public RouterFunction<?> mcpRouterFunction(SseServerTransport transport) {
        return transport.getRouterFunction();
    }

    @Bean
    public McpAsyncServer sseMcpServer(McpTransport transport) {
        return createMcpServer(transport);
    }

    private McpAsyncServer createMcpServer(McpTransport transport) {// @formatter:off
        // Configure server capabilities with resource support
        var capabilities = McpSchema.ServerCapabilities.builder()
                .tools(true) // Tool support with list changes notifications
                .build();

        // Create the server with both tool and resource capabilities
        return McpServer.using(transport)
                .info("MCP Demo Server", "1.0.0")
                .capabilities(capabilities)
                .tools(
                        themeParkApiDestinationToolRegistration(),
                        themeParkApiEntityScheduleToolRegistration(),
                        themeParkApiEntityLiveToolRegistration())
                .async();
    } // @formatter:on

    // ===================================================================================

    //
    // Theme Park API Destination Tool Registration
    //
    private static McpServer.ToolRegistration themeParkApiDestinationToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool("destinations", "Get a list of destinations including resorts and their respective theme parks. Each entry also includes the resort's or park's entity ID.",
                        """
                                {
                                    "type": "object",
                                    "properties": {
                                        "resort": {
                                            "type": "string",
                                            "description": "Filter results by resort name"
                                        }
                                    },
                                    "required": []
                                }
                                """),
                (arguments) -> {
                    var restClient = RestClient.builder().baseUrl("https://api.themeparks.wiki/v1").build();
                    var jsonResponse = restClient.get()
                                    .uri("/destinations")
                                    .retrieve()
                                    .body(String.class);
                    McpSchema.TextContent content = new McpSchema.TextContent(jsonResponse);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

    //
    // Theme Park API Entity Schedule Tool Registration
    //
    private static McpServer.ToolRegistration themeParkApiEntityScheduleToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool("entity-schedule", "Return schedule data about an entity, including hours of operation",
                        """
                                {
                                    "type": "object",
                                    "properties": {
                                        "entityId": {
                                            "type": "string",
                                            "description": "Entity ID"
                                        }
                                    },
                                    "required": ["entityId"]
                                }
                                """),
                (arguments) -> {
                    String entityId = (String) arguments.get("entityId");

                    var restClient = RestClient.builder().baseUrl("https://api.themeparks.wiki/v1").build();
                    var jsonResponse = restClient.get()
                            .uri("/entity/{entityId}/schedule", entityId)
                            .retrieve()
                            .body(String.class);
                    McpSchema.TextContent content = new McpSchema.TextContent(jsonResponse);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

    //
    // Theme Park API Entity Live Tool Registration
    //
    private static McpServer.ToolRegistration themeParkApiEntityLiveToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool("entity-live", "Return live data about attractions and shows, including show times and attraction wait times",
                        """
                                {
                                    "type": "object",
                                    "properties": {
                                        "entityId": {
                                            "type": "string",
                                            "description": "Entity ID"
                                        }
                                    },
                                    "required": ["entityId"]
                                }
                                """),
                (arguments) -> {
                    String entityId = (String) arguments.get("entityId");

                    var restClient = RestClient.builder().baseUrl("https://api.themeparks.wiki/v1").build();
                    var jsonResponse = restClient.get()
                            .uri("/entity/{entityId}/live", entityId)
                            .retrieve()
                            .body(String.class);
                    McpSchema.TextContent content = new McpSchema.TextContent(jsonResponse);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

}
