from mcp import ClientSession, StdioServerParameters
from mcp.client.stdio import stdio_client
from langchain_mcp_adapters.tools import load_mcp_tools
from langgraph.prebuilt import create_react_agent
from langchain_openai import ChatOpenAI
from dotenv import load_dotenv
import asyncio
import os


load_dotenv()

model = ChatOpenAI(
    model="gpt-4.1",  # Corregido: era "gtp-4.1"
    temperature=0,
    openai_api_key=os.getenv("OPENAI_API_KEY")
)

server_params = StdioServerParameters(
    command="npx",
    env={
        "FIRECRAWL_API_KEY": os.getenv("FIRECRAWL_API_KEY")
    },
    args=["firecrawl-mcp"]
)

async def main():
    async with stdio_client(server_params) as (read, write):  # Corregido: indentación consistente
        async with ClientSession(read, write) as session:  # Corregido: indentación consistente
            await session.initialize()
            tools = await load_mcp_tools(session)
            agent = create_react_agent(model, tools)

            messages = [
                {
                    "role": "system",  # Corregido: espacio después de ":"
                    "content": "You are a helpful assistant that can scrape websites, crawl pages, and extract data using Firecrawl tools. Think step by step and use the appropriate tools to help the user."       
                }
            ]

            print("Available tools:", *[tool.name for tool in tools])  # Corregido: formato más limpio
            print("-" * 60)  # Corregido: espacios alrededor de *
   
            while True:
                user_input = input("\nYou: ")  # Corregido: espacio después de ":"
                if user_input == "quit":
                    print("GoodBye")
                    break

                messages.append({"role": "user", "content": user_input[:175000]})  # Corregido: espacios después de ":"

                try:
                    agent_response = await agent.ainvoke({"messages": messages})  # Corregido: "messages" en plural e indentación

                    ai_message = agent_response["messages"][-1].content
                    print("\nAgent:", ai_message)  # Corregido: espacio después de ":"
                except Exception as e:
                    print("Error:", e)  # Corregido: espacio después de ":"

if __name__ == "__main__":  # Corregido: faltaban los dos puntos
    asyncio.run(main())