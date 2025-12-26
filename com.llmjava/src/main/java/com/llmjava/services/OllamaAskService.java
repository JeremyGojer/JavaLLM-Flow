package com.llmjava.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.llmjava.data.MessageHistoryManager;
import com.llmjava.dto.ChatRequest;
import com.llmjava.dto.GenerateRequest;
import com.llmjava.dto.GenerateResponse;
import com.llmjava.dto.Message;
import com.llmjava.exceptions.OllamaException;
import com.llmjava.utils.ImageUtils;
import com.llmjava.utils.JsonConverterUtil;
import com.llmjava.utils.OllamaClientReactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OllamaAskService {

	private String model;
	private long [] currentContext;
	private OllamaClientReactive ollamaClientReactive;
	private MessageHistoryManager messageHistoryManager;
	private JsonConverterUtil jsonConverterUtil;
	
	public OllamaAskService(String model) {
		this.model = model;
		this.jsonConverterUtil = new JsonConverterUtil();
		ollamaClientReactive = new OllamaClientReactive();
		messageHistoryManager = new MessageHistoryManager();
	}
	
	public OllamaAskService(String model, String systemInstruction) {
		this.model = model;
		this.jsonConverterUtil = new JsonConverterUtil();
		ollamaClientReactive = new OllamaClientReactive();
		messageHistoryManager = new MessageHistoryManager(systemInstruction);
	}
	
	public Flux<String> askWithContext(String prompt) {
        GenerateRequest request = GenerateRequest.builder().model(model).prompt(prompt).context(this.currentContext).build();

        return ollamaClientReactive.streamGenerate(request)
            .doOnNext(res -> {
                if (res.getDone() && res.getContext() != null) {
                    this.currentContext = res.getContext();
                }
            })
            .map(GenerateResponse::getResponse);
    }
	
	public Flux<String> askWithMessages(String content) {
		Message message = new Message("user", content);
		messageHistoryManager.append(message);
		ChatRequest request = ChatRequest.builder().model(model).messages(messageHistoryManager.getMessageHistory()).build();

		StringBuilder assistantResponseBuffer = new StringBuilder();
		
		return ollamaClientReactive.streamChat(request)
	            .map(res -> {
	                if (res.getMessage() != null && res.getMessage().getContent() != null) {
	                    String token = res.getMessage().getContent();
	                    assistantResponseBuffer.append(token);
	                    return token;
	                }
	                return "";
	            })
	            .filter(token -> !token.isEmpty())
	            .doOnComplete(() -> {
	                messageHistoryManager.append(new Message("assistant", assistantResponseBuffer.toString()));
	            });
	}
	
	public <T> Flux<String> askWithMessages(String content, Class<T> responseClass) {
		Message message = new Message("user", content);
		messageHistoryManager.append(message);
		ChatRequest request = ChatRequest.builder().model(model).messages(messageHistoryManager.getMessageHistory()).formatAs(responseClass).build();

		StringBuilder assistantResponseBuffer = new StringBuilder();
		
		return ollamaClientReactive.streamChat(request)
	            .map(res -> {
	                if (res.getMessage() != null && res.getMessage().getContent() != null) {
	                    String token = res.getMessage().getContent();
	                    assistantResponseBuffer.append(token);
	                    return token;
	                }
	                return "";
	            })
	            .filter(token -> !token.isEmpty())
	            .doOnComplete(() -> {
	                messageHistoryManager.append(new Message("assistant", assistantResponseBuffer.toString()));
	            });
	}
	
	public <T> Mono<T> askForObject(String prompt, Class<T> responseType) {
        return askWithMessages(prompt, responseType) 
                .collectList()
                .map(list -> String.join("", list))
                .map(fullJson -> {
                    try {
                        return jsonConverterUtil.fromJson(fullJson, responseType);
                    } catch (Exception e) {
                        throw new OllamaException("Failed to parse LLM response into " + responseType.getSimpleName(), e);
                    }
                });
	}
	
	public Flux<String> askWithImage(String prompt, String base64Image) throws IOException {
	    Message message = new Message();
	    message.setRole("user");
        message.setContent(prompt);
        message.setImages(List.of(base64Image));
        messageHistoryManager.append(message);
        
        ChatRequest request = ChatRequest.builder().model(model).messages(messageHistoryManager.getMessageHistory()).build();

		StringBuilder assistantResponseBuffer = new StringBuilder();
		
		return ollamaClientReactive.streamChat(request)
	            .map(res -> {
	                if (res.getMessage() != null && res.getMessage().getContent() != null) {
	                    String token = res.getMessage().getContent();
	                    assistantResponseBuffer.append(token);
	                    return token;
	                }
	                return "";
	            })
	            .filter(token -> !token.isEmpty())
	            .doOnComplete(() -> {
	                messageHistoryManager.append(new Message("assistant", assistantResponseBuffer.toString()));
	            });
	}
}
