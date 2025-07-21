package com.langchain4j;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface AssistantUser {

    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
