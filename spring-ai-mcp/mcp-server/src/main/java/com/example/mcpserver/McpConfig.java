package com.example.mcpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.mcp.server.McpAsyncServer;
import org.springframework.ai.mcp.server.McpServer;
import org.springframework.ai.mcp.server.transport.SseServerTransport;
import org.springframework.ai.mcp.server.transport.StdioServerTransport;
import org.springframework.ai.mcp.spec.McpSchema;
import org.springframework.ai.mcp.spec.ServerMcpTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;
import java.util.Map;

@Configuration
public class McpConfig {

    private static final String COMMON_INPUT_SCHEMA = """
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
                                """;

    //    This doesn't work, even though it is in line with the Map-based schema in the Spring AI MCP sample
    //    application. (See https://github.com/spring-projects-experimental/spring-ai-mcp/blob/main/spring-ai-mcp-sample/src/main/java/org/springframework/ai/mcp/sample/server/McpServerConfig.java#L135)
    //    private static final Map<String, Object> COMMON_INPUT_SCHEMA_MAP = Map.of("entityId", "String");

    //    This *DOES* work and is, in fact, the Map that is ultimately created when presenting the schema as a JSON string
    //    as shown in COMMON_INPUT_SCHEMA above.
    private static final Map<String, Object> COMMON_INPUT_SCHEMA_MAP =
            Map.of("type", "object", "properties",
                    Map.of("entityId",
                            Map.of("type", "string", "description", "Entity ID")), "required",
                    List.of("entityId"));



    @Autowired
    ThemeParkService themeParkService;

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
    public McpAsyncServer sseMcpServer(ServerMcpTransport transport) {// @formatter:off
        // Configure server capabilities with resource support
        var capabilities = McpSchema.ServerCapabilities.builder()
                .tools(true) // Tool support with list changes notifications
                .build();

        // Create the server with both tool and resource capabilities
        return McpServer.using(transport)
                .serverInfo("MCP Demo Server", "1.0.0")
                .capabilities(capabilities)
                .tools(
                        themeParkApiParksToolRegistration(),
                        themeParkApiEntityScheduleToolRegistration(),
                        themeParkApiEntityLiveToolRegistration())
                .async();
    } // @formatter:on

    // ===================================================================================
    //
    // Theme Park API Destination Tool Registration
    //
    private McpServer.ToolRegistration themeParkApiParksToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool(
                        "parks",
                        "Get a list of all parks, including their respective resorts and entity IDs",
                        Map.of()),
                (arguments) -> {
                    var parksJson = themeParkService.getParks();
                    McpSchema.TextContent content = new McpSchema.TextContent(parksJson);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

    //
    // Theme Park API Entity Schedule Tool Registration
    //
    private McpServer.ToolRegistration themeParkApiEntityScheduleToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool(
                        "entity-schedule",
                        "Return schedule data about an entity, including hours of operation",
                        COMMON_INPUT_SCHEMA_MAP),
                (arguments) -> {
                    String entityId = (String) arguments.get("entityId");
                    var entityScheduleJson = themeParkService.getEntitySchedule(entityId);
                    McpSchema.TextContent content = new McpSchema.TextContent(entityScheduleJson);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

    //
    // Theme Park API Entity Live Tool Registration
    //
    private McpServer.ToolRegistration themeParkApiEntityLiveToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool(
                        "entity-live",
                        "Return live data about attractions and shows, including show times and attraction wait times",
                        COMMON_INPUT_SCHEMA_MAP),
                (arguments) -> {
                    String entityId = (String) arguments.get("entityId");
                    var entityScheduleJson = themeParkService.getEntityLive(entityId);
                    McpSchema.TextContent content = new McpSchema.TextContent(entityScheduleJson);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

    //
    // Theme Park API Entity Children Tool Registration
    //
    private McpServer.ToolRegistration themeParkApiEntityChildrenToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool(
                        "entity-children",
                        "Get the entity IDs of the children of a specified entity. This is useful for getting the entity IDs of attractions within a theme park",
                        COMMON_INPUT_SCHEMA_MAP),
                (arguments) -> {
                    String entityId = (String) arguments.get("entityId");
                    var entityChildrenJson = themeParkService.getEntityChildren(entityId);
                    McpSchema.TextContent content = new McpSchema.TextContent(entityChildrenJson);
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

}
