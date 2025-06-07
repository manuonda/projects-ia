import * as dotenv from 'dotenv';
import { ChatOpenAI } from "@langchain/openai";
import { z } from 'zod';
import { tool } from '@langchain/core/tools';
import { ToolNode } from '@langchain/langgraph/prebuilt';
import { MessageGraph, MessagesAnnotation, START, END,StateGraph } from '@langchain/langgraph';
import { SystemMessage, HumanMessage } from '@langchain/core/messages';
import * as fs from "fs"


dotenv.config();


const llm = new ChatOpenAI({
    model: 'gpt-4o',
    temperature: 0.7,
    maxTokens: 1000,
    openAIApiKey: process.env.OPENAI_API_KEY
})

const getLastMessage = ({messages}) => messages[messages.length - 1];


const gmtTimeSchema = z.object({
    city : z.string().describe("The name of the city to get the GMT time for"),
})

const weatherApiSchema = z.object({
   city: z.string().describe("The name of the city"),
});

const gmtTimeTool = tool(
    async({city}) => {
        const serviceIsWorking = Math.floor(Math.random() * 3)
        return serviceIsWorking !== 2 
        ? `The local time in ${city} is 6:30pm`:
        'ERROR 404'
    },{
        name:"gmtTime",
        description: "Get the GMT time for a given city",
        schema: gmtTimeSchema,
    }
)


const tools = [gmtTimeTool];
const toolNode = new ToolNode(tools)
const llmWithTools = llm.bindTools(tools)

const callModelNode = async(state) => {
    const {messages} = state;
    //const llmWithTools = llm.bindTools([gmtTimeTool]);
    const result = await llmWithTools.invoke(messages);
    return {messages: [result]}
}

const shouldContinue = (state) => {
    const lastMessage = getLastMessage(state);
    const didAICalledAnyTools = lastMessage._getType() === 'ai' &&
    lastMessage.tool_calls?.length
    return didAICalledAnyTools ? "tools" : END;
}

const graph = new StateGraph(MessagesAnnotation)
    .addNode("agent", callModelNode)
    .addNode("tools", toolNode)
    .addEdge(START, "agent")
    .addEdge("tools", "agent")
    .addConditionalEdges("agent", shouldContinue,["tools",END])
  
const runnable = graph.compile();
const result = await runnable.invoke({
    messages: [  
        new SystemMessage("You are responsible "+
            " for answering user" 
            + " questions using tools. These tools sometimes fail" 
            + ", but you keep trying until you get a valid response."), 
            new HumanMessage("What is the time now in Singapore?" 
                + " I would like to call a friend who lives there.") 
        ]
})
console.log("AI : "+ getLastMessage(result).content);

const image = await runnable.getGraph().drawMermaidPng() 
const arrayBuffer = await image.arrayBuffer() 
await fs.writeFileSync( "graph-struct.png", new Uint8Array(arrayBuffer) )