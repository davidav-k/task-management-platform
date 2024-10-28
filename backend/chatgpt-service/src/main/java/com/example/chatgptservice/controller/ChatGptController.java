package com.example.chatgptservice.controller;

import com.example.chatgptservice.service.ChatGptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chatgpt")
public class ChatGptController {

    private final ChatGptService chatGptService;

    public ChatGptController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping("/prompt")
    public ResponseEntity<String> getResponseFromChatGpt(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String response = chatGptService.getChatGptResponse(prompt);
        return ResponseEntity.ok(response);
    }
}

