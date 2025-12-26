package com.llmjava.exceptions;

public class OllamaClientException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public OllamaClientException(String message) { super(message); }
    public OllamaClientException(String message, Throwable cause) { super(message, cause); }
}
