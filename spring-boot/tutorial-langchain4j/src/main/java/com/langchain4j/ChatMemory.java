package com.langchain4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.langchain4j.model.chat.ChatModel;
import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

import com.langchain4j.Assistant;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;



@RestController
@RequestMapping("chat")
public class ChatMemory {



    

    @GetMapping("/memory")
    public String getMethodName(@RequestParam("message") String param) {

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(250);

        System.out.println(ApiKeys.OPENAI_API_KEY);
        ChatModel model = OpenAiChatModel.builder()
        .apiKey(ApiKeys.OPENAI_API_KEY)
        .modelName(GPT_4_O_MINI)
        .build();

        Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(model)
        .chatMemory(chatMemory)
        .build();


        return  assistant.chat(param);
        
    }
    
}
