package utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Utility class for reading environment variables.
 *
 * Supports:
 * - .env file (local development)
 * Used for secure handling of API credentials.
 */
public class EnvReader {

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMalformed()
            .ignoreIfMissing()
            .load();

    private EnvReader() {}

    public static String get(String key) {
        String value = dotenv.get(key);

        if (value == null || value.isBlank()) {
            throw new RuntimeException("Missing environment variable: " + key);
        }

        return value;
    }
}