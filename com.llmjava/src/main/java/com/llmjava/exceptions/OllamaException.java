package com.llmjava.exceptions;

public class OllamaException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public OllamaException(String message) { super(message); }
    public OllamaException(String message, Throwable cause) { super(message, cause); }
}
