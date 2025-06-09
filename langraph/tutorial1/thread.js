import { HumanMessage } from "@langchain/core/messages";
import { END,START, MessagesAnnotation,MemorySaver, StateGraph } from "@langchain/langgraph";
import {ChatOpenAI} from "@langchain/openai"


import * as dotenv from "dotenv"
dotenv.load()

const llm = new ChatOpenAI({
    model:"gpt-4o",
    temperature:0
})

const getLastMessage = ({ messages }) => 
    messages[messages.length -1]

const callModelNode = async(state) => {
    const {messages} = state
    const result = await llm.invoke(messages)
    return { messages : [result] }
}

const workflow = new StateGraph(MessagesAnnotation)
.add