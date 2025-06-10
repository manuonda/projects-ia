import { HumanMessage } from "@langchain/core/messages";
import { END,START, MessagesAnnotation,MemorySaver, StateGraph } from "@langchain/langgraph";
import {ChatOpenAI} from "@langchain/openai"


import * as dotenv from "dotenv"
dotenv.config()

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
.addNode("agent", callModelNode)
.addEdge(START, "agent")
.addEdge("agent",END)


// initialize memory to persis state between graph runs
const checkpointer = new MemorySaver()
const graph = workflow.compile({checkpointer})

console.log("Starting first thread")
const configIntroThread = {
    configurable : { thread_id : "t1"}
}

//First lets introducer ourserlve to the AI 
const t1r1 = await graph.invoke({
    messages: [
        new HumanMessage("hi! My name is Daniel and I like LangGraph")

    ],
}, configIntroThread);
console.log(getLastMessage(t1r1));

//Does the Ai remember us ?
const t1r2 = await graph.invoke({
    messages:[
      new HumanMessage("soyrr, did I already introudce myself")
    ],
}, configIntroThread)
console.log(getLastMessage(t1r2).content)


// Yes you did! you mentioned that your name is Daniel 
// and that you like langgraph
console.log("Starting second thread")
const configAnotherThread = {
    configurable: { thread_id: "t2"}
}