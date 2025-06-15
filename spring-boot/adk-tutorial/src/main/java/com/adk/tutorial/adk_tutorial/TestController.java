package com.adk.tutorial.adk_tutorial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.GoogleSearchTool;


@RestController
@RequestMapping("/api/v1")
public class TestController {

    @GetMapping
    public String getMethodName() {
        LlmAgent rootAgent = LlmAgent.builder()
        .name("search_assistant")
        .model("gpt-4")
        .instruction("Eres un agente que permite buscar en la web")
        .tools(new GoogleSearchTool())
        .build();

        
        return null;
    }
    
}
