# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Structure

This is a multi-language AI projects repository with the following main components:

- **langraph/**: LangGraph AI agent tutorials and examples
  - `freecodecamp/`: Python notebooks for FreeCodeCamp LangGraph tutorials
  - `langchain/module-1/`: LangChain integration examples
  - `tutorial1/`: JavaScript/Node.js LangGraph tutorials
  - `tutorial_agents/`: Advanced agent examples
- **python/**: Python AI agent projects
  - `python-ai-agent-tutorial/simple-agent/`: Basic Python agent implementation
- **spring-boot/**: Spring Boot AI applications
  - `spring-ia/`: Spring AI integration project
  - `adk-tutorial/`: ADK tutorial application

## Common Development Commands

### Python Projects (uv-based)
All Python projects use `uv` as the package manager:

```bash
# Install dependencies
uv sync

# Run Python scripts
uv run python main.py

# Run Jupyter notebooks
uv run jupyter notebook
```

### JavaScript/Node.js Projects
Located in `langraph/tutorial1/`:

```bash
# Install dependencies
npm install

# Run specific examples
node index.js
node first_agent.js
node stategraph.js
```

### Spring Boot Projects
Located in `spring-boot/` subdirectories:

```bash
# Build project
./mvnw clean compile

# Run application
./mvnw spring-boot:run

# Run tests
./mvnw test
```

## Project Dependencies

### Python Stack
- **LangGraph**: Core graph-based agent framework
- **LangChain**: LLM application framework
- **OpenAI**: GPT model integration
- **MCP (Model Context Protocol)**: Tool integration
- **Jupyter**: Interactive development environment

### JavaScript Stack
- **@langchain/langgraph**: LangGraph JavaScript implementation
- **@langchain/openai**: OpenAI integration
- **langchain**: Core LangChain library
- **dotenv**: Environment variable management

### Spring Boot Stack
- **Spring AI**: Spring framework AI integration
- **Spring Boot 3.5.0**: Application framework
- **OpenAI Starter**: OpenAI model integration
- **PGVector**: Vector database support
- **JDBC Chat Memory**: Persistent chat memory

## Architecture Notes

### Agent Development Pattern
Most projects follow a common pattern:
1. **State Management**: Define agent state structures
2. **Tool Integration**: Connect external tools and APIs
3. **Graph Construction**: Build agent workflow graphs
4. **Memory Management**: Implement conversation memory
5. **Execution Flow**: Handle agent execution loops

### Environment Configuration
All projects require environment variables:
- `OPENAI_API_KEY`: OpenAI API access
- Additional project-specific variables in `.env` files

### Notebook Development
Jupyter notebooks are used extensively for:
- Interactive agent development
- Tutorial demonstrations
- Experimental workflows
- Memory and state management examples

## Testing and Development

### Running Individual Components
- Python notebooks: Use `uv run jupyter notebook` in project directory
- JavaScript examples: Run specific `.js` files with `node`
- Spring Boot: Use Maven wrapper for build/run/test cycles

### Common File Patterns
- `main.py`: Entry point for Python projects
- `pyproject.toml`: Python project configuration
- `package.json`: Node.js project configuration
- `pom.xml`: Maven project configuration
- `.env`: Environment variables (gitignored)