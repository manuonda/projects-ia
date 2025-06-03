import { FunctionMessageChunk } from "@langchain/core/messages";
import {END, START, MessageGraph} from "@langchain/langgraph"
import * as fs from "fs"


const funcA = input => {
    input[0].content += "Agent takes action A;";
    return input
}

const funcB = input => {
    input[0].content += "Agent takes action B;"
    return input
}

//build the graph 
const graph = new MessageGraph()
.addNode("nodeA", funcA)
.addNode("nodeB", funcB)

//edges
.addEdge(START, "nodeA")
.addEdge("nodeA","nodeB")
.addEdge("nodeB", END);

const runnable = graph.compile()
const result = await runnable.invoke("Input;")
console.log(result)


// Conditiona Edges in LangGraph 
const funBuy = input => {
    console.log("input funBuy , ", input)
    input[0].content += "= => Agent will buy stocks"
    return input
}

const funSell = input => {
    input[0].content += "==> Agent will sell stocks"
    return input
}

const funDecision  = input => {
    const last = input[0].content
    const isMarketDown =  last.includes("SP500") && last.includes("down")
    return isMarketDown?
      "actionBuyStocks": "actionShellSocks"
}

const graph2 = new MessageGraph();

// add nodes 
graph2.addNode("decision", funDecision)
.addNode("actionBuyStocks",funBuy)
.addNode("actionSellSocks",funSell)

//setup edges
graph2.addEdge(START, "decision")
.addConditionalEdges(
    "decision",
    funDecision,
  [ "actionBuyStocks","actionSellSocks"]
  )
  .addEdge("actionBuyStocks",END)
   .addEdge("actionSellSocks", END);

const runnable2 = graph.compile()
const result2 = await runnable.invoke("Latest news SP500 is down to 500");
console.log(result2);