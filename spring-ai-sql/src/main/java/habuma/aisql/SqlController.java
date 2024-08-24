package habuma.aisql;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqlController {
    @Value("classpath:/schema.sql")
    private Resource ddlResource;

    @Value("classpath:/sql-prompt-template.st")
    private Resource sqlPromptTemplateResource;

    private final ChatClient aiClient;
    private final JdbcTemplate jdbcTemplate;

    public SqlController(
            ChatClient.Builder aiClientBuilder,
            JdbcTemplate jdbcTemplate) {
        this.aiClient = aiClientBuilder.build();
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(path="/sql")
    public Answer sql(@RequestBody SqlRequest sqlRequest) throws IOException {
        String schema = ddlResource.getContentAsString(Charset.defaultCharset());

        String query = aiClient.prompt()
            .user(userSpec -> userSpec
                .text(sqlPromptTemplateResource)
                .param("question", sqlRequest.question())
                .param("ddl", schema)
                )
            .call()
            .content();

        if (query.toLowerCase().startsWith("select")) {
            return new Answer(query, jdbcTemplate.queryForList(query));
        }

        throw new SqlGenerationException(query);
    }

    public record SqlRequest(String question) {}

}
