package com.langchain4j;


import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import dev.langchain4j.model.chat.StreamingChatModel;



record ChatDTO(String message){}


@RestController
@RequestMapping("/chat/v2")
public class StreamingChat {


    @Autowired
    private StreamingChatModel streamingChatModel;

    @PostMapping("/stream", produces= MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<StreamingResponseBody> basicStream(@RequestBody ChatDTO entity) {
        StreamingResponseBody stream = outputStream -> {
          try(PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))){

          }
        };
        return null;
    }
    
}
