package com.example.mcpclient;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.customizer.McpSyncClientCustomizer;
import org.springframework.stereotype.Component;

@Component
public class McpConfig  implements McpSyncClientCustomizer {

  @Override
  public void customize(String name, McpClient.SyncSpec spec) {

    spec.sampling((McpSchema.CreateMessageRequest messageRequest) -> {
      var systemMessage = messageRequest.systemPrompt();
      return McpSchema.CreateMessageResult.builder()
          .message("Hello, world!")
          .build();
    });
  }

}
