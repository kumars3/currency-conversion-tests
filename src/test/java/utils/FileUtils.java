package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file and directory operations.
 *
 * Tasks:
 * - Create directories if not present
 * - Sanitise file names for safe usage
 * - Handle file-related helper operations
 *
 * Note:
 * Used for managing screenshots, traces, videos, and report files.
 */

public class FileUtils {

    private FileUtils() {
    }

    public static Path ensureDirectory(String directoryPath) {
        try {
            Path path = Paths.get(directoryPath);
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }

    public static String sanitizeFileName(String value) {
        return value.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}