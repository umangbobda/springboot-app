package com.app.playerservicejava.exception;

/**
 * Custom runtime exception representing failures while interacting with the AI model (Ollama/TinyLlama).
 * This exception wraps any low-level errors (e.g., network, IO, or model-specific) that occur
 * during AI inference, so higher layers can handle them uniformly.
 */
public class AiModelException extends RuntimeException {

    /**
     * Creates a new AiModelException with a descriptive message.
     *
     * @param message the detail message describing the error
     */
    public AiModelException(String message) {
        super(message);
    }

    /**
     * Creates a new AiModelException with a descriptive message and the underlying cause.
     *
     * @param message the detail message describing the error
     * @param cause   the root cause (e.g., IOException, InterruptedException)
     */
    public AiModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

