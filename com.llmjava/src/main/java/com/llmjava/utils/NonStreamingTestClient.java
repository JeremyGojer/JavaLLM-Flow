package com.llmjava.utils;

import java.net.http.HttpClient;
import java.util.List;

import com.google.gson.Gson;
import com.llmjava.dto.EmbeddingRequest;

public class NonStreamingTestClient {
    private static final String BASE_URL = "http://localhost:11434";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson(); 

    public static void main(String[] args) {
        try {
            // Use the model you just pulled
        	HttpClientUtil httpClientUtil = new HttpClientUtil();
        	
//        	GenerateRequest generateRequest = GenerateRequest.builder().model("llama3").prompt("Hello There").build();
        	
//        	String result = httpClientUtil.generate(generateRequest);
//        	System.out.println(result);
        	
//        	GenerateRequestContext generateRequestStream = GenerateRequestContext.builder().model("llama3").prompt("Hello There").stream(true).build();
//        	MetricsResponse metricsResponse = httpClientUtil.getMetricsResponse(generateRequestStream);
//        	System.out.println(new Gson().toJson(metricsResponse));
        	
        	EmbeddingRequest request = EmbeddingRequest.builder()
                    .model("nomic-embed-text")
                    .input(List.of("Hello There")) // Send the single text as a list for compatibility
                    .build();
        	String response = httpClientUtil.generateEmbedding(request);
        	System.out.println(response);
        	
//        	Message message = new Message("user", "Hello there");
//        	ChatRequest generateRequestStream = ChatRequest.builder().model("llama3").messages(List.of(message)).stream(true).build();
//        	Stream<String> metricsResponse = httpClientUtil.streamChat(generateRequestStream);
//        	System.out.println(metricsResponse.collect(Collectors.toList()));
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
