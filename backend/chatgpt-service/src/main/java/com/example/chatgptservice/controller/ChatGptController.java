package com.example.chatgptservice.controller;

import com.example.chatgptservice.dto.Result;
import com.example.chatgptservice.dto.StatusCode;
import com.example.chatgptservice.service.ChatGptService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}/chatgpt")
public class ChatGptController {

    private final ChatGptService chatGptService;

    public ChatGptController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping("/prompt")
    public Result getResponseFromChatGpt(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String response = chatGptService.getChatGptResponse(prompt);
        return new Result(true, StatusCode.SUCCESS, "Connect with chatgpt success", response);
    }
}

