package com.llmjava.dto;

import java.util.List;

public class ChatRequest extends AbstractOllamaRequest{
	
	private final List<Message> messages;

    private ChatRequest(Builder builder) {
        super(builder);
        this.messages = builder.messages;
    }

    public List<Message> getMessages() {
		return messages;
	}

	public static Builder builder() { return new Builder(); }

    public static class Builder extends AbstractOllamaRequest.Builder<Builder, ChatRequest> {
        private List<Message> messages;

        @Override
        protected Builder self() { return this; }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        @Override
        public ChatRequest build() {
            return new ChatRequest(this);
        }
    }
}
