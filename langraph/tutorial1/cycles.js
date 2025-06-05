import { START, END, MessageGraph, GraphBubbleUp } from "@langchain/langgraph";
import { HumanMessage } from "@langchain/core/messages";


const funAgent = input =>input 
const funUseDiceTool = input => {
    const dice = 1+ Math.floor(Math.random() * 6)
    const content = ( dice != 6) ? 'Dice rolled : '+ dice : 'objective_achieved'
    input.push(new HumanMessage(content))
    return input
}


const shouldContinue = input => {
    return input.pop().content.includes('objective_achieved')?
    'end': 'useDiceTool'
}


const graph = new MessageGraph()

graph.addNode('agent', funAgent)
.addNode('useDiceTool', funUseDiceTool)

graph.addEdge(START,'agent')
.addEdge('useDiceTool','agent')
.addConditionalEdges('agent',
    shouldContinue,
    {'useDiceTool': 'useDiceTool' , 'end': END} 
)

const runnable = graph.compile()
const result  = await runnable.invoke('Start Game')
console.log(result)


