package com.spring.ia.spring_ia;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

record ActorFilms(String actor, List<String> movies){}

@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatClient ChatClient;

    public ChatController(ChatClient.Builder chatClientBuilder){
        this.ChatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat/simple")
    public String getMethodName(@RequestParam("param") String param) {
        return this.ChatClient
        .prompt()
        .user(param)
        .call()
        .content();
    }

    @GetMapping("/chat/film")
    public ActorFilms getMethodName(){

         return this.ChatClient.prompt()
        .user("Generate the filmography for a random actor")
        .call()
        .entity(ActorFilms.class);
    }

    @GetMapping("/chat/films")
    public List<ActorFilms> getListActorFilms(){
        return  this.ChatClient.prompt()
        .user("Generate the filmography for a random actor")
        .call()
        .entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
   
    }
    
    

    
    
}
