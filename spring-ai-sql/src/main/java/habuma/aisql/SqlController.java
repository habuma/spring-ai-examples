package habuma.aisql;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

@RestController
public class SqlController {
    @Value("classpath:/schema.sql")
    private Resource ddlResource;

    private final PromptTemplate sqlPromptTemplate;

    private final ChatClient aiClient;
    private final JdbcTemplate jdbcTemplate;

    public SqlController(
            @Value("classpath:/sql-prompt-template.st") Resource sqlPromptTemplateResource,
            ChatClient aiClient,
            JdbcTemplate jdbcTemplate) {
        this.aiClient = aiClient;
        this.jdbcTemplate = jdbcTemplate;
        this.sqlPromptTemplate = new PromptTemplate(sqlPromptTemplateResource);
    }

    @PostMapping(path="/sql")
    public Answer sql(@RequestBody SqlRequest sqlRequest) throws IOException {
        String schema = ddlResource.getContentAsString(Charset.defaultCharset());
        Prompt prompt = sqlPromptTemplate.create(Map.of("question", sqlRequest.question(), "ddl", schema));
        String query = aiClient.call(prompt).getResult().getOutput().getContent();

        if (query.toLowerCase().startsWith("select")) {
            return new Answer(query, jdbcTemplate.queryForList(query));
        }

        throw new SqlGenerationException(query);
    }

    public record SqlRequest(String question) {}

}
