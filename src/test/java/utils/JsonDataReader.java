package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ConversionInput;

import java.io.InputStream;
import java.util.List;

/**
 * Utility class for reading currency conversion inputs from JSON.
 * Converts JSON data into ConversionInput objects.
 */

public class JsonDataReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<ConversionInput> readConversions(String resourcePath) {
        try (InputStream input = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new RuntimeException("Test data file not found: " + resourcePath);
            }

            return mapper.readValue(input, new TypeReference<List<ConversionInput>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON test data from " + resourcePath, e);
        }
    }
}