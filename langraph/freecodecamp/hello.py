from typing import Dict, TypedDict
from langgraph.graph import StateGraph



class AgentState(TypedDict):
    message: str


def greet_node(state: AgentState) -> AgentState:
    """ Simple node that adds a greeting message to the state """
    state['message'] = "Hey "+ state["message"] + ", how si your day going?"
    return state
    

graph = StateGraph(AgentState)
graph.add_node("greeter", greet_node)

graph.set_entry_point("greeter")
graph.set_finish_point("greeter")


app = graph.compile()

from IPython.display import Image, display
display(Image(app.get_graph().draw_mermaid_png()))

