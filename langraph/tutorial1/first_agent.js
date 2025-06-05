import * as dotenv from 'dotenv';
import { ChatOpenAI } from "@langchain/openai";
import { z } from 'zod';
import { tool } from '@langchain/core/tools';
import { ToolNode } from '@langchain/langgraph/prebuilt';
import { MessageGraph, MessagesAnnotation, START, StateGraph } from '@langchain/langgraph';

dotenv.config();


const llmNode = new ChatOpenAI({
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

const toolNode = new ToolNode([gmtTimeTool])


const graph = new StateGraph(MessagesAnnotation)
    .addNode("agent", llmNode)
    .addNode("tools", toolNode)
    .addEdge(START, "agent")
    .addEdge("tools", "agent")
    .addConditionalEdges("agent", shouldContinue,["tools",END])
  
const runnable = graph.compile();

const image = await runnable.getGraph().drawMermaidPng() 
const arrayBuffer = await image.arrayBuffer() 
await fs.writeFileSync( "graph-struct.png", new Uint8Array(arrayBuffer) )