package com.example.chatgptservice.execption;

public class ChatGptPromptException extends RuntimeException {
    public ChatGptPromptException(String message) {
        super(message);
    }
}
