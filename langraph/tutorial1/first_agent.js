import * as dotenv from 'dotenv';
import { ChatOpenAI } from "@langchain/openai";
import { z } from 'zod';
import { tool } from '@langchain/core/tools';
import { ToolNode } from '@langchain/langgraph/prebuilt';
import { MessageGraph, MessagesAnnotation, START, StateGraph } from '@langchain/langgraph';

dotenv.config();


const llm = new ChatOpenAI({
    model: 'gpt-4o',
    temperature: 0.7,
    maxTokens: 1000,
    openAIApiKey: process.env.OPENAI_API_KEY
})

const gmtTimeSchema = z.object({
    city : z.string().describe("The name of the city to get the GMT time for"),
})

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

const tool = new ToolNode([gmtTimeTool])


const graph = new StateGraph(MessagesAnnotation)
    .addNode("agent", llm)
    .addNode("tools", tool)
    .addEdge(START, "agent")
    .addEdge("tools", "agent")
    .addConditionalEdges("agent", shouldContinue,["tools",END])
    .compile();