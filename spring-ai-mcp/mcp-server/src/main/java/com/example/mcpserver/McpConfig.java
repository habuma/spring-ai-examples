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
                .resources(false, true) // No subscribe support, but list changes notifications
                .tools(true) // Tool support with list changes notifications
                .prompts(true) // Prompt support with list changes notifications
                .build();

        // Create the server with both tool and resource capabilities
        return McpServer.using(transport)
                .info("MCP Demo Server", "1.0.0")
                .capabilities(capabilities)
                .resources(systemInfoResourceRegistration())
                .prompts(greetingPromptRegistration())
                .tools(weatherToolRegistration(), calculatorToolRegistration())
                .async();
    } // @formatter:on

    // ===================================================================================

    //
    // Resource Registration
    //
    private static McpServer.ResourceRegistration systemInfoResourceRegistration() {

        // Create a resource registration for system information
        var systemInfoResource = new McpSchema.Resource( // @formatter:off
                "system://info",
                "System Information",
                "Provides basic system information including Java version, OS, etc.",
                "application/json", null
        );

        var resourceRegistration = new McpServer.ResourceRegistration(systemInfoResource, (request) -> {
            try {
                var systemInfo = Map.of(
                        "javaVersion", System.getProperty("java.version"),
                        "osName", System.getProperty("os.name"),
                        "osVersion", System.getProperty("os.version"),
                        "osArch", System.getProperty("os.arch"),
                        "processors", Runtime.getRuntime().availableProcessors(),
                        "timestamp", System.currentTimeMillis());

                String jsonContent = new ObjectMapper().writeValueAsString(systemInfo);

                return new McpSchema.ReadResourceResult(
                        List.of(new McpSchema.TextResourceContents(request.uri(), "application/json", jsonContent)));
            }
            catch (Exception e) {
                throw new RuntimeException("Failed to generate system info", e);
            }
        }); // @formatter:on

        return resourceRegistration;
    }

    //
    // Prompt Registration
    //
    private static McpServer.PromptRegistration greetingPromptRegistration() {

        var prompt = new McpSchema.Prompt("greeting", "A friendly greeting prompt",
                List.of(new McpSchema.PromptArgument("name", "The name to greet", true)));

        return new McpServer.PromptRegistration(prompt, getPromptRequest -> {

            String nameArgument = (String) getPromptRequest.arguments().get("name");
            if (nameArgument == null) {
                nameArgument = "friend";
            }

            var userMessage = new McpSchema.PromptMessage(McpSchema.Role.USER,
                    new McpSchema.TextContent("Hello " + nameArgument + "! How can I assist you today?"));

            return new McpSchema.GetPromptResult("A personalized greeting message", List.of(userMessage));
        });
    }

    //
    // Weather Tool Registration
    //
    private static McpServer.ToolRegistration weatherToolRegistration() {
        return new McpServer.ToolRegistration(
                new McpSchema.Tool("weather", "Weather forecast tool by location",
//                        Map.of("city", "String")
        """
        {
            "type": "object",
            "properties": {
                "city": {
                    "type": "string",
                    "description": "The city to get the weather for"
                }
            },
            "required": ["city"]
        }
        """
                ),
                (arguments) -> {
                    String city = (String) arguments.get("city");
                    McpSchema.TextContent content = new McpSchema.TextContent("Weather forecast for " + city + " is sunny");
                    return new McpSchema.CallToolResult(List.of(content), false);
                });
    }

    //
    // Calculator Tool Registration
    //
    private static McpServer.ToolRegistration calculatorToolRegistration() {
        return new McpServer.ToolRegistration(new McpSchema.Tool("calculator",
                "Performs basic arithmetic operations (add, subtract, multiply, divide)", """
						{
							"type": "object",
							"properties": {
								"operation": {
									"type": "string",
									"enum": ["add", "subtract", "multiply", "divide"],
									"description": "The arithmetic operation to perform"
								},
								"a": {
									"type": "number",
									"description": "First operand"
								},
								"b": {
									"type": "number",
									"description": "Second operand"
								}
							},
							"required": ["operation", "a", "b"]
						}
						"""), arguments -> {
            String operation = (String) arguments.get("operation");
            double a = (Double) arguments.get("a");
            double b = (Double) arguments.get("b");

            double result;
            switch (operation) {
                case "add":
                    result = a + b;
                    break;
                case "subtract":
                    result = a - b;
                    break;
                case "multiply":
                    result = a * b;
                    break;
                case "divide":
                    if (b == 0) {
                        return new McpSchema.CallToolResult(
                                java.util.List.of(new McpSchema.TextContent("Division by zero")), true);
                    }
                    result = a / b;
                    break;
                default:
                    return new McpSchema.CallToolResult(
                            java.util.List.of(new McpSchema.TextContent("Unknown operation: " + operation)),
                            true);
            }

            return new McpSchema.CallToolResult(
                    java.util.List.of(new McpSchema.TextContent(String.valueOf(result))), false);
        });
    }

}
