package com.llmjava.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ImageUtils {
	
	public static String encodeToBase64(Path imagePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(imagePath);
        return Base64.getEncoder().encodeToString(fileContent);
    }
	
}
