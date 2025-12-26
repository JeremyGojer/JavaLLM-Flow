package com.llmjava.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.stream.Stream;

import com.llmjava.dto.GenerateRequest;

public class OllamaClientStreaming {
    
    private final HttpClient httpClient; 
    private final JsonConverterUtil jsonConverter = new JsonConverterUtil();
    
    public OllamaClientStreaming() {
		httpClient = HttpClient.newHttpClient();
	}
    
    public Stream<String> streamGenerate(GenerateRequest request, String baseUrl) throws IOException, InterruptedException {
        
        // 1. Ensure the request DTO is set to stream=true
        GenerateRequest streamingRequest = request.builder().stream(true).build(); 
        String jsonPayload = jsonConverter.toJson(streamingRequest);
        
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/generate"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
        
        // 2. Use BodyHandlers.ofLines() for line-by-line streaming
        HttpResponse<Stream<String>> response = httpClient.send(
                httpRequest,
                BodyHandlers.ofLines() // IMPORTANT: Returns the body as a Stream<String>
        );

        if (response.statusCode() != 200) {
            // Read the error body if available, and throw an exception
            String errorBody = response.body().findFirst().orElse("Unknown error");
            throw new IOException("Ollama Streaming Error: " + response.statusCode() + " - " + errorBody);
        }

        // 3. The Stream<String> must then be mapped to your DTOs
        // Each String in this stream is one NDJSON chunk!
        return response.body(); 
        // You will later chain a .map(json -> jsonConverter.fromJson(json, GenerateResponse.class)) here
    }
}
