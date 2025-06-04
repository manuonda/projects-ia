package com.spring.ia.spring_ia.memorybasic;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class MemoryControllerAdvisorMessages {


    private ChatClient chatClient;

    public MemoryControllerAdvisorMessages(ChatClient.Builder builder, ChatMemory chatMemory){
        this.chatClient = builder.clone()
        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        .build();
    }

    @PostMapping("/memory/messages/{conversationId}")
    String chat(@PathVariable String conversationId, @RequestBody String question) {
        return chatClient.prompt()
                .user(question)
                .advisors(a -> a.param(CONVERSATION_ID, conversationId))
                .call()
                .content();
    }

}
