package com.example.demo;

import org.springaicommunity.agent.tools.ShellTools;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatClientConfig {

  @Value("classpath:/.claude/skills")
  Resource skillPath;

  @Bean
  ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    return chatClientBuilder
        .defaultTools(
            ShellTools.builder().build())
        .defaultToolCallbacks(SkillsTool.builder()
            .addSkillsResource(skillPath)
            .build())
        .defaultAdvisors(ToolCallAdvisor.builder().build())
        .build();
  }

}
