package com.llmjava.utils;

import java.io.IOException;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.llmjava.dto.ChatRequest;
import com.llmjava.dto.EmbeddingRequest;
import com.llmjava.dto.EmbeddingResponse;
import com.llmjava.dto.GenerateRequest;
import com.llmjava.dto.GenerateResponse;
import com.llmjava.exceptions.OllamaException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OllamaClientReactive {
	HttpClientUtil httpClientUtil = new HttpClientUtil();
    private final Gson gson = new Gson();

    public Flux<GenerateResponse> streamGenerate(GenerateRequest request){
    	
    	return Flux.<GenerateResponse,Stream<String>>using(
    	        () -> {
    	            try {
    	                // Assuming httpClientUtil returns a Stream<String>
    	                return httpClientUtil.streamGenerate(request);
    	            } catch (IOException | InterruptedException e) {
    	                throw new RuntimeException("Failed to initiate Ollama stream", e);
    	            }
    	        },
    	        stream -> Flux.fromStream(stream)
    	            .filter(line -> !line.isBlank())
    	            .map(line -> gson.fromJson(line, GenerateResponse.class)),
    	        Stream::close
    	    )
    	    .subscribeOn(Schedulers.boundedElastic())
    	    .onErrorMap(e -> new OllamaException("Streaming failed", e));
    }
    
    public Flux<GenerateResponse> streamChat(ChatRequest request) {
        return Flux.<GenerateResponse,Stream<String>>using(
            () -> {
                try {
                    return httpClientUtil.streamChat(request); 
                } catch (Exception e) {
                    throw new OllamaException("Chat streaming failed", e);
                }
            },
            stream -> Flux.fromStream(stream)
                .filter(line -> !line.isBlank())
                .map(line -> gson.fromJson(line, GenerateResponse.class)),
            Stream::close
        ).subscribeOn(Schedulers.boundedElastic())
        .onErrorMap(e -> new OllamaException("Streaming failed", e));
    }
    
    public Flux<String> streamPrompt(String model, String prompt) {
        GenerateRequest request = GenerateRequest.builder().model(model).prompt(prompt).build();
        return streamGenerate(request)
                .map(GenerateResponse::getResponse)
                .filter(text -> text != null && !text.isEmpty());
    }
    
    public Mono<EmbeddingResponse> embed(EmbeddingRequest request) {
    	return Mono.fromCallable(() -> {
            return httpClientUtil.generateEmbedding(request); 
        })
        .subscribeOn(Schedulers.boundedElastic())
        .map(json -> gson.fromJson(json, EmbeddingResponse.class))
        .onErrorMap(e -> new OllamaException("Embedding failed", e));
    }

}
