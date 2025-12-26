package com.llmjava.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SchemaGenerator {
	
    public static Map<String, Object> generate(Class<?> clazz) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        
        Map<String, Object> properties = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            properties.put(field.getName(), Map.of("type", mapType(field.getType())));
        }
        
        schema.put("properties", properties);
        schema.put("required", new ArrayList<>(properties.keySet()));
        return schema;
    }

    private static String mapType(Class<?> type) {
        if (type == String.class) return "string";
        if (type == int.class || type == Integer.class || type == long.class) return "integer";
        if (type == boolean.class || type == Boolean.class) return "boolean";
        if (type == double.class || type == Double.class) return "number";
        return "string";
    }
}
