package com.langchain4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.List;
import java.util.Map;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;
import static dev.langchain4j.data.message.ChatMessageDeserializer.messagesFromJson;
import static dev.langchain4j.data.message.ChatMessageSerializer.messagesToJson;


import org.mapdb.DB;
import org.mapdb.DBMaker;
import static org.mapdb.Serializer.STRING;

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


    @GetMapping("/memoryUser")
    public void windowChatMemoryUser() {
       ChatModel chatModel = OpenAiChatModel.builder()
                             .apiKey(ApiKeys.OPENAI_API_KEY)
                             .modelName(GPT_4_O_MINI)
                             .build();
                     
        AssistantUser assistantUser = AiServices.builder(AssistantUser.class)
        .chatModel(chatModel)
        .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(100))
        .build();

        System.out.println(assistantUser.chat(1, "Hello my name is Klaus"));
        System.out.println(assistantUser.chat(2, "Hello my name is David"));
        System.out.println(assistantUser.chat(1, "Cual es mi nombre"));
        System.out.println(assistantUser.chat(2, "Cual es mi nombre"));
    }

    @GetMapping("/persistenMemory")
    public void persistenMemory() {

        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(new PersistentChatMemoryStore())
                .build();

        ChatModel chatModel  = OpenAiChatModel.builder()
        .apiKey(ApiKeys.OPENAI_API_KEY)
        .modelName(GPT_4_O_MINI)
        .build();


        Assistant assistant = AiServices.builder(Assistant.class)
        .chatModel(chatModel)
        .chatMemory(chatMemory)
        .build();


        String answer = assistant.chat("Hello! My name is Klaus.");
        System.out.println(answer); // Hello Klaus! How can I assist you today?

        // Now, comment out the two lines above, uncomment the two lines below, and run again.

        // String answerWithName = assistant.chat("What is my name?");
        // System.out.println(answerWithName); // Your name is Klaus.        
    }

    @GetMapping("/persistentMemoryUser")
    public void persistentMemoryUser() {
        MessageWindowChatMemory chatMemory =  MessageWindowChatMemory.builder()
        .maxMessages(10)
        .chatMemoryStore(new PersistentChatMemoryStore())
        .build();

        ChatModel model = OpenAiChatModel.builder()
        .modelName(GPT_4_O_MINI)
        .apiKey(ApiKeys.OPENAI_API_KEY)
        .build();

        AssistantUser assistantUser = AiServices.builder(AssistantUser.class)
        .chatModel(model)
        .chatMemory(chatMemory)
        .build();

        System.out.println(assistantUser.chat(1, "Hola mi nombre es David"));
        System.out.println(assistantUser.chat(2, "hola mi nombre es Andres"));
        System.out.println(assistantUser.chat(1, "Cual es mi nombre"));
        System.out.println(assistantUser.chat(2,"Cual es mi nombre?"));

    }

    @GetMapping("/streaming")
    public void streamingChatModel() {
        StreamingChatModel model = OpenAiStreamingChatModel.builder()
        .apiKey(ApiKeys.OPENAI_API_KEY)
        .modelName(GPT_4_O_MINI)
        .build();

        String userMessage = "Tell me a joke";

        model.chat(userMessage, new StreamingChatResponseHandler() {

            @Override
            public void onPartialResponse(String partialResponse) {
               
                System.out.println("onPartilResponse : " + partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
               System.out.println("onCompleteResponse :" + completeResponse);
            }

            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
            }
            
        });

    }
    
    

      // You can create your own implementation of ChatMemoryStore and store chat memory whenever you'd like
    static class PersistentChatMemoryStore implements ChatMemoryStore {

        private final DB db = DBMaker.fileDB("chat-memory.db").transactionEnable().make();
        private final Map<String, String> map = db.hashMap("messages", STRING, STRING).createOrOpen();

        @Override
        public List<ChatMessage> getMessages(Object memoryId) {
            String json = map.get((String) memoryId);
            return messagesFromJson(json);
        }

        @Override
        public void updateMessages(Object memoryId, List<ChatMessage> messages) {
            String json = messagesToJson(messages);
            map.put((String) memoryId, json);
            db.commit();
        }

        @Override
        public void deleteMessages(Object memoryId) {
            map.remove((String) memoryId);
            db.commit();
        }
    }
    
    
}
