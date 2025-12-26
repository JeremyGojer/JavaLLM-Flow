package com.llmjava.dto;

import com.llmjava.dto.GenerateRequest.Builder;

//temperature, top_k, top_p, num_predict, and seed
public class Options {

	private final String temperature;
	private final String top_k;
	private final String top_p;
	private final String num_predict;
	private final String seed;
	
	private Options(Builder builder) {
		this.temperature = builder.temperature;
		this.top_k = builder.top_k;
		this.top_p = builder.top_p;
		this.num_predict = builder.num_predict;
		this.seed = builder.seed;
	} 
	
	public static Builder builder() {
        return new Builder();
    }
	
	public String getTemperature() {
		return temperature;
	}

	public String getTop_k() {
		return top_k;
	}

	public String getTop_p() {
		return top_p;
	}

	public String getNum_predict() {
		return num_predict;
	}

	public String getSeed() {
		return seed;
	}

	public static class Builder{
		private String temperature;
		private String top_k;
		private String top_p;
		private String num_predict;
		private String seed;
		
		public Options build() {
			return new Options(this);
		}
		
		public Builder temperature(String temperature) {
			this.temperature = temperature;
			return this;
		}
		
		public Builder top_k(String top_k) {
			this.top_k = top_k;
			return this;
		}
		
		public Builder top_p(String top_p) {
			this.top_p = top_p;
			return this;
		}
		
		public Builder num_predict(String num_predict) {
			this.num_predict = num_predict;
			return this;
		}
		
		public Builder seed(String seed) {
			this.seed = seed;
			return this;
		}
		
	}
}
