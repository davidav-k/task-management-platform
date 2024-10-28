package com.example.chatgptservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "dev")
class ChatGptServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ChatGptService chatGptService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
//        chatGptService = new ChatGptService(restTemplate);
    }


    @Test
    public void testGetChatGptResponse_Success() {

        String prompt = "Tell me a joke";
        String expectedResponse = "{\"choices\":[{\"text\":\"Here is a joke!\"}]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        String response = chatGptService.getChatGptResponse(prompt);

        assertEquals(expectedResponse, response);
    }










//    @Test
//    public void testGetChatGptResponse_Success_() {
//
//        String prompt = "Tell me a joke";
//        ResponseEntity<String> responseEntity = new ResponseEntity<>(prompt, HttpStatus.OK);
//
//        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
//                .thenReturn(responseEntity);
//
//        String response = chatGptService.getChatGptResponse(prompt);
//
//        assertEquals(prompt, response);
//    }

    @Test
    public void testGetChatGptResponse_TooManyRequests() {
        // Мокируем ошибку превышения лимита запросов
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS));

        String prompt = "Tell me a joke";
        String actualResponse = chatGptService.getChatGptResponse(prompt);

        assertEquals("Error: Rate limit exceeded. Try again later.", actualResponse);
    }

    @Test
    public void testGetChatGptResponse_ApiError() {
        // Мокируем общую ошибку API
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid request"));

        String prompt = "Tell me a joke";
        String actualResponse = chatGptService.getChatGptResponse(prompt);

        assertTrue(actualResponse.contains("Error: API error"));
    }

    @Test
    public void testGetChatGptResponse_PromptIsEmpty() {
        // Тестируем случай, когда передан пустой prompt
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            chatGptService.getChatGptResponse("");
        });

        assertEquals("Prompt cannot be null or empty", thrown.getMessage());
    }
}
