package com.llmjava.services;

import java.util.List;

import com.llmjava.data.MessageHistoryManager;
import com.llmjava.dto.EmbeddingRequest;
import com.llmjava.dto.EmbeddingResponse;
import com.llmjava.utils.OllamaClientReactive;

import reactor.core.publisher.Mono;

public class EmbeddingService {

	private String model;
	private OllamaClientReactive ollamaClientReactive;
	
	public EmbeddingService(String model) {
		this.model = model;
		ollamaClientReactive = new OllamaClientReactive();
	}
	
	public EmbeddingService(String model, String systemInstruction) {
		this.model = model;
		ollamaClientReactive = new OllamaClientReactive();
	}
	
	public Mono<EmbeddingResponse> getEmbed(String... input) {
		EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().model(model).input(List.of(input)).build();
		return ollamaClientReactive.embed(embeddingRequest);
	}
}
