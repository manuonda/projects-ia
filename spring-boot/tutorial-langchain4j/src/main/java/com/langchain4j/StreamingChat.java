package com.langchain4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.model.chat.StreamingChatModel;



record ChatDTO(String message){}


@RestController
@RequestMapping("/chat/v2")
public class StreamingChat {


    @Autowired
    private StreamingChatModel streamingChatModel;

    @PostMapping("/stream")
    public String postMethodName(@RequestBody ChatDTO entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
