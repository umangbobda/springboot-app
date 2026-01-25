FROM ollama/ollama:latest

# Set the environment variable for where models are stored
ENV OLLAMA_MODELS=/root/.ollama/models

# Start Ollama in the background, pull the model, then kill the process
RUN nohup bash -c "ollama serve &" && \
    sleep 5 && \
    ollama pull tinyllama && \
    pkill ollama

# Expose the default port
EXPOSE 11434

# Set the entrypoint
ENTRYPOINT ["ollama", "serve"]