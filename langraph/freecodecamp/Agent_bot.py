from typing import TypedDict, Type , List
from langchain_core.messages import HumanMessage
from langchain_openai import ChatOpenAI
from langgraph.graph import StateGraph, START, END
from dotenv import load_dotenv

load_dotenv("./.env")

class AgentState(TypedDict):
    """ State of the agent. """
    messages: List[HumanMessage]
    
llm = ChatOpenAI(
    model="gpt-4o",
    temperature=0.0,
    max_tokens=1000
)


def process(state: AgentState) -> AgentState:
    """ Process the state invoke message y show response the content """
    response = llm.invoke(state['messages'])
    print(f"\nAI : {response.content}")
    return state

graph = StateGraph(AgentState)
graph.add_node("process",process)
graph.add_edge(START, "process")
graph.add_edge("process", END)

agent = graph.compile()
user_input = input("Enter: ")
agent.invoke({ "messages": [HumanMessage(content=user_input)] })