package com.spring.ia.spring_ia;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

record ActorFilms(String actor, List<String> movies){}

@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    private ChatMemory chatMemory;

    public ChatController(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder
        //.defaultAdvisors(null)
        .build();
    }

    @GetMapping("/chat/simple")
    public String getMethodName(@RequestParam("param") String param) {
        return this.chatClient
        .prompt()
        .user(param)
        .call()
        .content();
    }

    @GetMapping("/chat/film")
    public ActorFilms getMethodName(){

         return this.chatClient.prompt()
        .user("Generate the filmography for a random actor")
        .call()
        .entity(ActorFilms.class);
    }

    @GetMapping("/chat/films")
    public List<ActorFilms> getListActorFilms(){
        return  this.chatClient.prompt()
        .user("Generate the filmography for a random actor")
        .call()
        .entity(new ParameterizedTypeReference<List<ActorFilms>>() {});
   
    }


    @GetMapping("/chat/template")
    public String getChatTemplate() {
        return this.chatClient.prompt()
        .user( u->  u.text("Tell me the names of 5 movies whose soundtrack was compose by {composer}")
        .param("composer", "John Williams"))
        .call()
        .content();
    }





    @GetMapping("/chat/memory")
    public String getMemory(@RequestParam("memory") String param) {
        ChatMemoryRepository chatMemoryRepository =  new InMemoryChatMemoryRepository();
        //MessageChatMemoryAdvisor advisor = new MessageChatMemoryAdvisor(chatMemory);
         return this.chatClient.prompt()
         .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
         .user(param)
         .call()
         .content();
    }



    
    
    
    
    

    
    
}
