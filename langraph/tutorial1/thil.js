import {END, START } from "@langchain/core"
import { ChatOpenAI } from "@langchain/openai";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { HumanMessagePromptTemplate } from "@langchain/core/prompts";
import { Annotation, MessagesAnnotation, StateGraph } from "@langchain/langgraph";
import { HumanMessage } from "@langchain/core/messages";
import {z} from "zod";
import { tool } from "@langchain/core/tools";

import { ChatOpenAI } from "@langchain/openai";
import * as dotenv from "dotenv";
import * as reader from "readline-sync";
dotenv.config();


const llm = new ChatOpenAI({
    model: "gpt-4o",
    temperature: 0.1,
    maxTokens: 1000,
})

const callModelNode = async (state) => {
    const {messages } = state;
    const result = await llm.invoke(messages);
    return { messages : [result]}
}

const graphState =  Annotation.Root({
    ...MessagesAnnotation.spec,
    //whether or not permission has been granted
    // to use credit card
    askHumanUseCreditCard: Annotation(),
})

const purchaseTicketTool = tool(
    (input) => "Successfully purchased ticket for " + input.destination,
    {
        name: "purchase_ticket",
        description: "Buy a plane ticket for a given destination",
        schema: z.object({
            destination: z.string().describe("The destination of the planet ticket")
        }),
    }
    
)

const tools = [purchaseTicketTool];

const nodeTools = async (state) => {
    const {messages, askHumanUseCreditCard} = state;
    if(!askHumanUseCreditCard){
        throw new Error("Permission to use credit card is required")
    }

    const lastMessage = messages[messages.length  - 1]
    const toolCall = lastMessage.tool_calls[0]
    //invoke the tool to buy the planet ticket 
    const result = await purchaseTicketTool.invoke(toolCall.args);
    //return the result as a message
    return {messages : result}

}

const nodeAgent = async (state) => {
    const {messages} = state
    const llmWithTools =  llm.bindTools(tools)
    const result = await llmWithTools.invoke(messages);
    return { messages : [result] }
}

const shouldContinue = (state) => {
    const {messages} = state;
    const lastMessage = messages[messages.length - 1];
    if( lastMessage._getType() !=="ai" || !lastMessage.tool_calls?.length){
        //LLM did not call a tool,
        //or not AI message , so we have to end 
        return END;
    }
    return "tools";
}


const workflow = new StateGraph(graphState)




