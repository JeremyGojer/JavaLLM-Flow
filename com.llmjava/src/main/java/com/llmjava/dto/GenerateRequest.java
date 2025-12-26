package com.llmjava.dto;

public class GenerateRequest extends AbstractOllamaRequest{
	
	private final String prompt;
    private final long[] context;

    private GenerateRequest(Builder builder) {
        super(builder);
        this.prompt = builder.prompt;
        this.context = builder.context;
    }

    public String getPrompt() {
		return prompt;
	}

	public long[] getContext() {
		return context;
	}

	public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractOllamaRequest.Builder<Builder, GenerateRequest> {
        private String prompt;
        private long[] context;

        @Override
        protected Builder self() { return this; }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder context(long[] context) {
            this.context = context;
            return this;
        }

        @Override
        public GenerateRequest build() {
            return new GenerateRequest(this);
        }
    }

}
