package com.langchain4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

@Configuration
public class LangChain4jConfig {
    
    @Value("${OPENAI_API_KEY:demo}")
    private String openaiApiKey;
    
    @Bean
    public StreamingChatModel StreamingChatModel(){
        if ("demo".equals(openaiApiKey) || openaiApiKey == null || openaiApiKey.trim().isEmpty()) {
            throw new IllegalStateException("OPENAI_API_KEY environment variable must be set with a valid OpenAI API key");
        }
        System.out.println(ApiKeys.OPENAI_API_KEY);
        return OpenAiStreamingChatModel.builder()
        .apiKey(openaiApiKey)
        .modelName(GPT_4_O_MINI)
        .temperature(0.7)
        .build();
    }
   
}
