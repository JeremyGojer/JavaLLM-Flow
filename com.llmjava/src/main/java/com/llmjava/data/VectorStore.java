package com.llmjava.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.llmjava.utils.VectorUtils;

public class VectorStore {
	
	private final Map<String, List<Float>> storage = new HashMap<>();

    public void add(String text, List<Float> vector) {
        storage.put(text, vector);
    }

    public String findMostSimilar(List<Float> queryVector) {
        return storage.entrySet().stream()
            .max(Comparator.comparingDouble(entry -> 
                VectorUtils.cosineSimilarity(queryVector, entry.getValue())))
            .map(Map.Entry::getKey)
            .orElse("No context found");
    }
	
}
