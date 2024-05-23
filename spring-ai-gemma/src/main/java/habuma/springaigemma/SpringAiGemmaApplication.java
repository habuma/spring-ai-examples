package habuma.springaigemma;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatModel;
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
    ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

    @Bean
    RouterFunction<ServerResponse> routes(ChatClient chatClient) {
        return RouterFunctions.route()
            .GET("/ask", req -> {
                String answer = chatClient.prompt()
                    .user(req.param("question").orElse("tell me a joke"))
                    .call()
                    .content();

                return ServerResponse.ok().body(answer);
            })
            .build();
    }

}
