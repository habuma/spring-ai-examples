package com.example.demo
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
class AiConfig(@Value("classpath:/BurgerBattle-rules.pdf") val rules: Resource) {
    @Bean
    fun go(vectorStore: VectorStore): ApplicationRunner {
        return ApplicationRunner {
            vectorStore.add(
                TokenTextSplitter().apply(
                    TikaDocumentReader(rules).get()))
        }
    }
}