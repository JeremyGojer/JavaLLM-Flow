package com.llmjava.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.llmjava.dto.EmbeddingRequest;
import com.llmjava.dto.GenerateRequest;
import com.llmjava.dto.ChatRequest;
import com.llmjava.dto.GenerateResponse;
import com.llmjava.dto.MetricsResponse;
import com.llmjava.exceptions.ModelNotFoundException;
import com.llmjava.exceptions.OllamaClientException;

public class HttpClientUtil {

	private final HttpClient httpClient; 
	private final JsonConverterUtil jsonConverter = new JsonConverterUtil();
	private String baseUrl;

	public HttpClientUtil() {
		httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
		baseUrl = "http://localhost:11434";//TODO : Take URL from config file
	}


	public String generate(GenerateRequest request) throws IOException, InterruptedException {

		String jsonPayload = jsonConverter.toJson(request);

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/generate"))
				.header("Content-Type", "application/json")
				.timeout(java.time.Duration.ofSeconds(60)) // Set a request-specific timeout
				.POST(HttpRequest.BodyPublishers.ofString(jsonPayload)) // Attach the JSON body
				.build();

		HttpResponse<String> response = httpClient.send(
				httpRequest, 
				HttpResponse.BodyHandlers.ofString() // Collects the entire body as a String
				);

		checkAndThrowError(response, request.getModel());

		if (response.statusCode() != 200) {
			throw new IOException("Ollama API Error: " + response.statusCode() + " - " + response.body());
		}

		return response.body();
	}

	public Stream<String> streamGenerate(GenerateRequest request) throws IOException, InterruptedException {

		// 1. Ensure the request DTO is set to stream=true
		String jsonPayload = jsonConverter.toJson(request);

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/generate"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
				.build();

		HttpResponse<Stream<String>> response = httpClient.send(
				httpRequest,
				BodyHandlers.ofLines()
				);

		if (response.statusCode() != 200) {
			// Read the error body if available, and throw an exception
			String errorBody = response.body().findFirst().orElse("Unknown error");
			throw new IOException("Ollama Streaming Error: " + response.statusCode() + " - " + errorBody);
		}

		return response.body();
	}

	public Stream<String> streamChat(ChatRequest request) throws IOException, InterruptedException {

		// 1. Ensure the request DTO is set to stream=true
		String jsonPayload = jsonConverter.toJson(request);

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/chat"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
				.build();

		HttpResponse<Stream<String>> response = httpClient.send(
				httpRequest,
				BodyHandlers.ofLines()
				);

		if (response.statusCode() != 200) {
			// Read the error body if available, and throw an exception
			String errorBody = response.body().findFirst().orElse("Unknown error");
			throw new IOException("Ollama Streaming Error: " + response.statusCode() + " - " + errorBody);
		}

		return response.body();
	}

	public MetricsResponse getMetricsResponse(GenerateRequest generateRequest) throws IOException, InterruptedException {
		return getResponseMetrics(streamGenerate(generateRequest));
	}

	public MetricsResponse getResponseMetrics(Stream<String> streamResponse) {
		Stream<GenerateResponse> generateResponse = streamResponse.map(elem->jsonConverter.fromJson(elem, GenerateResponse.class));
		List<String> responses = new ArrayList<>();
		GenerateResponse metrics  = generateResponse.filter(elem->{
			responses.add(elem.getResponse());
			return elem.getDone();
		}).findFirst().orElseGet(null);
		MetricsResponse metricsResponse = new MetricsResponse();
		metricsResponse.setMetrics(metrics);
		metricsResponse.setResponses(responses);
		return metricsResponse;
	}

	public String generateEmbedding(EmbeddingRequest request) throws IOException, InterruptedException {

		String jsonPayload = jsonConverter.toJson(request);

		HttpRequest httpRequest = HttpRequest.newBuilder()
				.uri(URI.create(baseUrl + "/api/embed"))
				.header("Content-Type", "application/json")
				.timeout(java.time.Duration.ofSeconds(60)) // Set a request-specific timeout
				.POST(HttpRequest.BodyPublishers.ofString(jsonPayload)) // Attach the JSON body
				.build();

		HttpResponse<String> response = httpClient.send(
				httpRequest, 
				HttpResponse.BodyHandlers.ofString() // Collects the entire body as a String
				);

		checkAndThrowError(response, request.getModel());

		if (response.statusCode() != 200) {
			throw new IOException("Ollama API Error: " + response.statusCode() + " - " + response.body());
		}

		return response.body();
	}


	private void checkAndThrowError(HttpResponse<String> response, String requestedModel) {
		int status = response.statusCode();
		String body = response.body();

		if (status >= 200 && status < 300) {
			return; // Success
		}

		if (status == 404) {
			if (body != null && body.contains("model") && body.contains("not found")) {
				throw new ModelNotFoundException(requestedModel);
			}

			throw new OllamaClientException("Resource Not Found (404) at " + response.uri() + ". Details: " + body);
		}

		if (status == 400) {
			throw new OllamaClientException("Bad Request (400): Invalid request parameters. Details: " + body);
		}

		if (status >= 500 && status < 600) {
			throw new OllamaClientException("Ollama Server Error (" + status + "). Details: " + body);
		}

		throw new OllamaClientException("Unexpected HTTP Status " + status + ". Body: " + body);
	}
}
