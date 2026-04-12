package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for reading configuration values from framework.properties.
 *
 * Tasks:
 * - Load configuration file
 * - Provide access to configuration values (browser, URL, timeouts, etc.)
 *
 * Note:
 * Centralised configuration management helps avoid hard coding values
 * and supports easy environment-specific changes.
 */

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config/framework.properties")) {
            if (input == null) {
                throw new RuntimeException("framework.properties not found");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load framework.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}