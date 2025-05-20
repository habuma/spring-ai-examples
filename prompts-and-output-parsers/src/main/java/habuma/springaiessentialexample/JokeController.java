package habuma.springaiessentialexample;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {

    private final ChatClient chatClient;

    @Value("classpath:/joke-template.st")
    private Resource jokeTemplate;

    public JokeController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/joke")
    public JokeResponse tellJoke(@RequestParam(name="subject", defaultValue="penguins") String subject) {
        return chatClient.prompt()
            .user(userSpec -> userSpec
                .text(jokeTemplate)
                .param("subject", subject))
            .call()
            .entity(JokeResponse.class);
    }

}
