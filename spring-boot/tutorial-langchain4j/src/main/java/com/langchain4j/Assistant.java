package com.langchain4j;

import dev.langchain4j.service.SystemMessage;

interface  Assistant{
    @SystemMessage("Please answer funny")
    String chat(String message);
}