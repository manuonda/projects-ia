import { END, START, MessageGraph } from "@langchain/langgraph";
import * as fs from "fs"

const funcA = input => {input[0].content += "A"; return input }
const funcB = input => {input[0].content += "B"; return input }
const funcC = input => {input[0].content += "C"; return input }
const funcD = input => {input[0].content += "D"; return input }
const funcE = input => {input[0].content += "E"; return input }


const graph = new MessageGraph();

graph.addNode("nodeA",funcA)
.addNode("nodeB",funcB)
.addNode("nodeC",funcC)
.addNode("nodeD",funcD)
.addNode("funcE",funcE)

graph.addEdge(START, "nodeA")
.addEdge("nodeA", "nodeB")
.addEdge("nodeA", "nodeC")
.addEdge("nodeA","nodeD")
.addEdge("nodeB", "funcE")
.addEdge("nodeC", "funcE")
.addEdge("nodeD", "funcE")
.addEdge("funcE", END);

//print the graph to a file
const FILE_NAME ="graph-struct.png"
const runnable = graph.compile();
const image = await runnable.getGraph().drawMermaidPng();
const arrayBuffer = await image.arrayBuffer();
await fs.writeFileSync(FILE_NAME, new Uint8Array(arrayBuffer));
console.log(`Graph structure saved to ${FILE_NAME}`);