package habuma.springaifunctions;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.List;
import java.util.function.Function;

@SpringBootApplication
public class SpringAiFunctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiFunctionsApplication.class, args);
    }

    @Bean
    @Description("Get the wait time for a Disneyland attraction in minutes")
    public Function<WaitTimeService.Request, WaitTimeService.Response> getWaitTime() {
        return new WaitTimeService();
    }

    @Bean
    RouterFunction<ServerResponse> routes(ChatClient chatClient) {
        return RouterFunctions.route()
            .GET("/waitTime", req -> {
                String ride = req.param("ride").orElse("Space Mountain");

                UserMessage userMessage =
                        new UserMessage("What's the wait time for " + ride + "?");

                ChatResponse response = chatClient.call(new Prompt(
                        List.of(userMessage),
                        OpenAiChatOptions.builder()
                                .withFunction("getWaitTime")
                                .build()));

                return ServerResponse
                        .ok()
                        .body(response.getResult().getOutput().getContent());
            })
            .build();
    }

}
