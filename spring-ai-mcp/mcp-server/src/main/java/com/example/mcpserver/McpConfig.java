package com.example.mcpserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.mcp.server.McpAsyncServer;
import org.springframework.ai.mcp.server.McpServer;
import org.springframework.ai.mcp.server.transport.SseServerTransport;
import org.springframework.ai.mcp.server.transport.StdioServerTransport;
import org.springframework.ai.mcp.spec.McpSchema;
import org.springframework.ai.mcp.spec.ServerMcpTransport;
import org.springframework.ai.mcp.spring.ToolHelper;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.List;

@Configuration
public class McpConfig {

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
                .tools(themeParkApiToolRegistrations())
                .async();
    } // @formatter:on

    private List<McpServer.ToolRegistration> themeParkApiToolRegistrations() {
        var getParks = FunctionCallback.builder()
                .description("Get list of Parks")
                .method("getParks")
                .targetObject(themeParkService)
                .build();

        var getEntitySchedule = FunctionCallback.builder()
                .description("Get Park's entity schedule")
                .method("getEntitySchedule", String.class)
                .targetObject(themeParkService)
                .build();

        var getEntityLive = FunctionCallback.builder()
                .description("Get Park's entity live status")
                .method("getEntityLive", String.class)
                .targetObject(themeParkService)
                .build();

        var getEntityChildren = FunctionCallback.builder()
                .description("Get Park's entity children")
                .method("getEntityChildren", String.class)
                .targetObject(themeParkService)
                .build();

        return ToolHelper.toToolRegistration(getParks, getEntitySchedule, getEntityLive, getEntityChildren);
    }

}
