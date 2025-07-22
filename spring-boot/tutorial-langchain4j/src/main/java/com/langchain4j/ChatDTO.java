package com.langchain4j;

public class ChatDTO {
    private String message;
    
    public ChatDTO() {}
    
    public ChatDTO(String message) {
        this.message = message;
    }
    
    public String message() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
