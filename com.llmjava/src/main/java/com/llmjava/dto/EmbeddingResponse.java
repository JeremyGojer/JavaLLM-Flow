package com.llmjava.dto;

import java.util.List;

public class EmbeddingResponse {
    private List<List<Float>> embeddings;

    public List<Float> getVector() {
        if (embeddings != null && !embeddings.isEmpty()) {
            return embeddings.get(0);
        }
        return List.of();
    }
    
}
