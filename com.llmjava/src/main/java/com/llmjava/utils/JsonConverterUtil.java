package com.llmjava.utils;

import com.google.gson.Gson;

public class JsonConverterUtil {
	
	public Gson gson = new Gson();
	
	public <T> String toJson(T object) {
		return gson.toJson(object);
	}
	
	public <T> T fromJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
}
