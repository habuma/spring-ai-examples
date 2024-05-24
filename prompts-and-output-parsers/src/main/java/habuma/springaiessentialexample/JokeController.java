package habuma.springaiessentialexample;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {

    private String promptTemplate;

    private final ChatClient chatClient;

    public JokeController(ChatClient.Builder chatClientBuilder,
                          @Value("${app.promptTemplate}") String promptTemplate) {
        this.chatClient = chatClientBuilder.build();
        this.promptTemplate = promptTemplate;
    }

    @GetMapping("/joke")
    public JokeResponse tellJoke(@RequestParam("subject") String subject) {
        return chatClient.prompt()
            .user(userSpec -> userSpec
                .text(promptTemplate)
                .param("subject", subject))
            .call()
            .entity(JokeResponse.class);
    }

}
