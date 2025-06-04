package com.spring.ia.spring_ia;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.client.ChatClient;


import java.util.List;


@RestController
@RequestMapping("/api/users")
public class MultiUserInMemoryChatController {

    private final ChatClient chatClient;


    private final ChatMemory chatMemory;

    public MultiUserInMemoryChatController(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        this.chatClient = chatClientBuilder
        //.defaultAdvisors(InMemoryChatMemoryRepository())
        .build();
        this.chatMemory = chatMemory;
    }

    @PostMapping("/{userId}/chat")
    public String chat(
            @PathVariable String userId,
            @RequestBody String request) {

        return chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param("23", userId))
                .user(request)
                .call().content();
    }

    @GetMapping("/{userId}/history")
    public List<Message> getHistory(@PathVariable String userId) {
        return chatMemory.get(userId);
    }

    @DeleteMapping("/{userId}/history")
    public String clearHistory(@PathVariable String userId) {
        chatMemory.clear(userId);

        return "Conversation history cleared for user: " + userId;
    }
}