package com.app.playerservicejava.service.chat;

import com.app.playerservicejava.exception.AiModelException;
import io.github.ollama4j.OllamaAPI;
import io.github.ollama4j.exceptions.OllamaBaseException;
import io.github.ollama4j.models.Model;
import io.github.ollama4j.models.OllamaResult;
import io.github.ollama4j.types.OllamaModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.ollama4j.utils.OptionsBuilder;
import io.github.ollama4j.utils.PromptBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class ChatClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatClientService.class);
    private static final String MODEL = OllamaModelType.TINYLLAMA;
    private static String baseURL="baseURL";

    private final OllamaAPI ollamaAPI;

    public ChatClientService() {
        baseURL = System.getenv().getOrDefault(
                "OLLAMA_BASE_URL",
                "http://localhost:11434"
        );
        LOGGER.info("Base URL ========= {}", baseURL);
        this.ollamaAPI = new OllamaAPI(baseURL);
    }

    public List<Model> listModels() throws OllamaBaseException, IOException, URISyntaxException, InterruptedException {
        LOGGER.error("Base URL ========= {}", baseURL);
        List<Model> models = ollamaAPI.listModels();
        return models;
    }

    public String chat() throws OllamaBaseException, IOException, InterruptedException {
        String model = OllamaModelType.TINYLLAMA;

        // https://ollama4j.github.io/ollama4j/intro
        PromptBuilder promptBuilder =
                new PromptBuilder()
                        .addLine("Recite a haiku about recursion.");

        boolean raw = false;
        OllamaResult response = ollamaAPI.generate(model, promptBuilder.build(), raw, new OptionsBuilder().build());
        return response.getResponse();
    }

    /** ✅ New method — accepts a custom prompt */
    public String chatWithPrompt(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt cannot be null or empty");
        }

        try {
            PromptBuilder promptBuilder = new PromptBuilder().addLine(prompt);
            OllamaResult response = ollamaAPI.generate(MODEL, promptBuilder.build(), false, new OptionsBuilder().build());
            return response.getResponse();
        } catch (IOException | InterruptedException | OllamaBaseException e){
            LOGGER.error("AI model call failed", e);
            throw new AiModelException("AI model call failed", e);
        }
    }

}
