package habuma.springaifunctions;

import java.util.function.Function;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class SpringAiFunctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiFunctionsApplication.class, args);
    }

    @Bean
    ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
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

                String answer = chatClient.prompt()
                    .functions("getWaitTime")
                    .user("What's the wait time for " + ride + "?")
                    .call()
                    .content();

                return ServerResponse
                        .ok()
                        .body(answer);
            })
            .build();
    }

}
