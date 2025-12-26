package com.llmjava.dto;

import java.util.List;

public class MetricsResponse {
	private List<String> responses;
	private GenerateResponse metrics;
	
	public List<String> getResponses() {
		return responses;
	}
	public void setResponses(List<String> responses) {
		this.responses = responses;
	}
	public GenerateResponse getMetrics() {
		return metrics;
	}
	public void setMetrics(GenerateResponse metrics) {
		this.metrics = metrics;
	}
}
