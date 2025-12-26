package com.llmjava.dto;

import java.util.List;

import com.llmjava.dto.GenerateRequest.Builder;

public class EmbeddingRequest {
	private String model;
	private List<String> input;
	private boolean truncate;
	
	public EmbeddingRequest(Builder builder) {
		this.model = builder.model;
		this.input = builder.input;
		this.truncate = builder.truncate;
	}
	
	public static Builder builder() {
        return new Builder();
    }
	
	public String getModel() {
		return model;
	}
	
	public List<String> getInput() {
		return input;
	}

	public boolean isTruncate() {
		return truncate;
	}

	public static class Builder {
		
		private String model;
		private List<String> input;
		private boolean truncate;
		
		public EmbeddingRequest build() {
			return new EmbeddingRequest(this);
		}
		
		public Builder model(String model) {
			this.model = model;
			return this;		
		}
		
		public Builder input(List<String> input) {
			this.input = input;
			return this;		
		}
		
		public Builder truncate(boolean truncate) {
			this.truncate = truncate;
			return this;		
		}
	}
}
