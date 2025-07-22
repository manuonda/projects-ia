package com.langchain4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TutorialLangchain4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialLangchain4jApplication.class, args);
	
	    SimpleChatClient client = new SimpleChatClient();
		String[] questions = {
            "Hola",
            "Quiero que me digas sobre bleach", 
        };
        
        for (String q : questions) {
            System.out.println("🧪 TEST: " + q);
            client.ask(q);
            System.out.println("\n" + "=".repeat(40));
            
            try { Thread.sleep(2000); } catch (Exception e) {}
        }
	}

}
