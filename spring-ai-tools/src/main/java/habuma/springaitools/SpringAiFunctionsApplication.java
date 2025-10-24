package habuma.springaitools;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class SpringAiFunctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiFunctionsApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes(
          ChatClient.Builder chatClientBuilder,
          WaitTimeService waitTimeService) {
        return RouterFunctions.route()
            .GET("/waitTime", req -> {
                ChatClient chatClient = chatClientBuilder.build();

                String ride = req.param("ride").orElse("Space Mountain");
                String answer = chatClient.prompt()
                    .tools(waitTimeService)
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
