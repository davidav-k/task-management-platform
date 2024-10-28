package com.example.chatgptservice.service;

import com.example.chatgptservice.execption.ChatGptPromptException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatGptService {

    private final RestTemplate restTemplate;

    @Value("${openai.api-key}")
    private String openAiApiKey;

    @Value("${openai.api-url}")
    private String openAiApiUrl;

    public ChatGptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getChatGptResponse(@NotNull String  prompt) {
        if (prompt.isEmpty()) {
            throw new ChatGptPromptException("Prompt cannot be null or empty");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, request, String.class);
        return response.getBody();

    }
}


