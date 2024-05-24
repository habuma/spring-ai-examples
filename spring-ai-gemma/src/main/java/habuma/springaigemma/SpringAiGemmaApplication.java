package habuma.springaigemma;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

@SpringBootApplication
public class SpringAiGemmaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiGemmaApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes(ChatClient.Builder chatClientBuilder) {
        return RouterFunctions.route()
            .GET("/ask", req -> {
                ChatClient chatClient = chatClientBuilder.build();

                String answer = chatClient.prompt()
                    .user(req.param("question").orElse("tell me a joke"))
                    .call()
                    .content();

                return ServerResponse.ok().body(answer);
            })
            .build();
    }

}
