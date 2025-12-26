package com.llmjava.exceptions;

public class ModelNotFoundException extends OllamaClientException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModelNotFoundException(String modelName) {
        super("The model '" + modelName + "' was not found on the Ollama server. Did you run 'ollama pull " + modelName + "'?");
    }
}
