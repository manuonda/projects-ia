package com.spring.ia.spring_ia.memorybasic;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class MemoryControllerAdvisorPrompt {

    private final ChatClient chatClient;

    MemoryControllerAdvisorPrompt(ChatClient.Builder builder, ChatMemory chatMemory){
        this.chatClient = builder.clone()
        .defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build())
        .build();
    }

    @PostMapping("/memory/prompt/{conversationId}")
    public String postMethodName(@PathVariable String conversationId, 
    @RequestBody String question) {
    
        return this.chatClient.prompt()
        .user(question)
        .advisors(a -> a.param(CONVERSATION_ID, conversationId))
        .call()
        .content();
    }
    
}
