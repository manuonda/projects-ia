import {z} from "zod";
import { Tool } from "langchain/tools";
import { MessageGraph, MessagesAnnotation, START, END, StateGraph } from "@langchain/langgraph";
import { SystemMessage, HumanMessage } from "@langchain/core/messages";
import { ChatOpenAI } from "@langchain/openai";
import * as dotenv from "dotenv";
import { tool } from "@langchain/core/tools";
import { ToolNode } from "@langchain/langgraph/prebuilt";
import * as fs from "fs";


dotenv.config();

const llm = new ChatOpenAI({
    model: "gpt-4o",
    temperature: 0.7,
    maxTokens: 1000,
    openAIApiKey: process.env.OPENAI_API_KEY
})


const weatherApiSchema = z.object({
    city: z.string().describe("The name of the city"),
})

const hotelsAvailableSchema = z.object({
    city: z.string().describe("The name of the city"),
    day: z.string().describe("Day of the week to book the hotel")
})

const weatherApiTool = tool(
    async({city}) => {
       return `The weather in ${city} is sunny with 20 Celsius`; 
    },{
        name: "weatherApi",
        description: "Get the weather for a given city",
        schema: weatherApiSchema
    }
)

const hotelsAvailableTool = tool(
    async({city, day}) => {
        return `Hotels room in the ${city} are available for ${day}`;
    },{
        name: "hotelsAvailable",
        description: "Check if hotels are available in a given city for a given day",
        schema: hotelsAvailableSchema
    }
)

