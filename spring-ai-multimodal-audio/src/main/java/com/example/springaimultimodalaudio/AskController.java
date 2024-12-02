package com.example.springaimultimodalaudio;

import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters.Voice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AskController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(AskController.class);

    private final ChatClient chatClient;

    public AskController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .withOutputModalities(List.of("text", "audio"))
                        .withOutputAudio(new AudioParameters(Voice.ALLOY, AudioResponseFormat.MP3))
                        .build())
                .build();
    }

    @PostMapping("/ask")
    public byte[] ask(@RequestParam("audio") MultipartFile audio) {
        Media questionAudio = new Media(
                MimeTypeUtils.parseMimeType("audio/mp3"), audio.getResource());

        ChatResponse chatResponse = chatClient.prompt()
                .user(userSpec -> userSpec
                        .text("Answer the question in the given audio file.")
                        .media(questionAudio))
                .call()
                .chatResponse();

        String answer = chatResponse.getResult().getOutput().getContent();
        log.info("Answer: {}", answer);

        return chatResponse.getResult().getOutput().getMedia().getFirst().getDataAsByteArray();
    }

}
