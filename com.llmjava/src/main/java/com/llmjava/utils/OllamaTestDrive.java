package com.llmjava.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.llmjava.data.VectorStore;
import com.llmjava.dto.GenerateRequest;
import com.llmjava.services.EmbeddingService;
import com.llmjava.services.OllamaAskService;
import com.llmjava.test.User;

public class OllamaTestDrive {
    public static void main(String[] args) throws IOException {

//    	testOllamaClientReactive();
//    	testOllamaAskWithContextService();
//    	testOllamaAskWithMessageService();
//    	testOllamaAskWithMessageServiceAndFormatObject();
//    	testOllamaAskWithMessageServiceAndProcessImage();
//    	testOllamaGetEmbed();
    	testStaticMiniRAG();
    }
    
    public static void testOllamaClientReactive() {
      OllamaClientReactive client = new OllamaClientReactive();
      String model = "llama3"; // Ensure you have this pulled
      String prompt = "Write a three-sentence story about a Java robot.";

      System.out.println("--- Testing streamPrompt (Tokens Only) ---");
      
      client.streamPrompt(model, prompt)
          .doOnNext(token -> {
              System.out.print(token);
              System.out.flush();
          })
          .doOnTerminate(() -> System.out.println("\n--- Stream Finished ---"))
          .doOnError(e -> System.err.println("Error: " + e.getMessage()))
          .blockLast(); // <--- CRITICAL: Prevents main from closing early

      System.out.println("\n--- Testing streamGenerate (Full DTOs) ---");

      GenerateRequest genReq = GenerateRequest.builder().model(model).prompt(prompt).build();
      client.streamGenerate(genReq)
          .doOnNext(res -> System.out.println("Received DTO: " + res.getResponse() + " | Done: " + res.getDone()))
          .blockLast();
    }
    
    public static void testOllamaAskWithContextService() {
    	String model = "llama3"; // Ensure you have this pulled
        OllamaAskService ollamaAskService = new OllamaAskService(model);
        
        System.out.println("--- Testing statement 1 ---");
        
        ollamaAskService.askWithContext("My name is Jeremy")
            .doOnNext(token -> {
                System.out.print(token);
                System.out.flush();
            })
            .doOnTerminate(() -> System.out.println("\n--- Stream Finished ---"))
            .doOnError(e -> System.err.println("Error: " + e.getMessage()))
            .blockLast();

        System.out.println("--- Testing statement 2 ---");
        
        ollamaAskService.askWithContext("What is my name ?")
        .doOnNext(token -> {
            System.out.print(token);
            System.out.flush();
        })
        .doOnTerminate(() -> System.out.println("\n--- Stream Finished ---"))
        .doOnError(e -> System.err.println("Error: " + e.getMessage()))
        .blockLast();
        
    }
    
    public static void testOllamaAskWithMessageService() {
    	String model = "llama3";
    	String personality = "doctor";
        OllamaAskService ollamaAskService = new OllamaAskService(model,personality);
        
        System.out.println("--- Testing statement 1 ---");
        
        ollamaAskService.askWithMessages("My name is Jeremy")
            .doOnNext(token -> {
                System.out.print(token);
                System.out.flush();
            })
            .doOnTerminate(() -> System.out.println("\n--- Stream Finished ---"))
            .doOnError(e -> System.err.println("Error: " + e.getMessage()))
            .blockLast();

        System.out.println("--- Testing statement 2 ---");
        
        ollamaAskService.askWithMessages("What is my name ?")
        .doOnNext(token -> {
            System.out.print(token);
            System.out.flush();
        })
        .doOnTerminate(() -> System.out.println("\n--- Stream Finished ---"))
        .doOnError(e -> System.err.println("Error: " + e.getMessage()))
        .blockLast();
        
    }
    
    public static void testOllamaAskWithMessageServiceAndFormatObject() {
    	String model = "llama3";
    	String personality = "doctor";
        OllamaAskService ollamaAskService = new OllamaAskService(model,personality);
        
        System.out.println("--- Testing statement 1 ---");
        
        ollamaAskService.askForObject("My name is Jeremy and my age is 29. Convert this data into json object", User.class)
        .doOnNext(user -> System.out.println("Mapped User: " + user.getName() + " (" + user.getAge() + ")"))
        .block();
        
    }
    
    public static void testOllamaAskWithMessageServiceAndProcessImage() throws IOException {
    	String model = "llama3.2-vision";
        OllamaAskService ollamaAskService = new OllamaAskService(model);
//        String imagePath = "fruits.jpg";
        String imagePath = "animals.webp";
        Path path = Path.of(imagePath);
        
        System.out.println("--- Testing statement 1 ---");
        String base64Image = ImageUtils.encodeToBase64(path);
             
        ollamaAskService.askWithImage("List the objects you understand in the image",base64Image)
        .doOnNext(token -> {
            System.out.print(token);
            System.out.flush();
        })
        .doOnTerminate(() -> System.out.println("\n--- Stream Finished ---"))
        .doOnError(e -> System.err.println("Error: " + e.getMessage()))
        .blockLast();
        
    }
    
    public static void testOllamaGetEmbed() {
    	String model = "nomic-embed-text";
    	EmbeddingService embeddingService = new EmbeddingService(model);
    	String input = "Hello there";
    	
    	embeddingService.getEmbed(input).doOnNext(res -> {
            List<Float> vector = res.getVector();
            System.out.println("Vector size: " + vector.size());
            System.out.println("First 5 values: " + vector.subList(0, 5));
        })
        .block();
    	
    }
    
    public static void testStaticMiniRAG() {
    	String model = "llama3";
    	EmbeddingService embeddingService = new EmbeddingService(model);
    	VectorStore vectorStore = new VectorStore();
    	Map<String,String> ingestion = new HashMap<>();
    	ingestion.put("Java", "In Java, a Final class cannot be subclassed, and a Final method cannot be overridden.");
    		ingestion.put("Cooking", "To make a perfect carbonara, use egg yolks, pecorino romano, and guanciale—never cream.");
    		ingestion.put("Space", "The James Webb Space Telescope orbits the sun at the second Lagrange point (L2).");
    	
//    	String prompt = "Tell me about class inheritance.";
//    	String prompt = "What is the best way to cook pasta?";
    	String prompt = "Where is the newest NASA telescope located?";
    	
    	for(String topic:ingestion.keySet()) {
    		vectorStore.add(topic, embeddingService.getEmbed(ingestion.get(topic)).block().getVector());
    	}
    	
    	String result = vectorStore.findMostSimilar(embeddingService.getEmbed(prompt).block().getVector());
    	
    	System.out.println(result);
    	
    }
    
    
}
