package com.jminiapp.core.utils;

import com.jminiapp.core.api.JMiniAppConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility for resolving file paths with security considerations.
 *
 * <p>This class provides path resolution that:</p>
 * <ul>
 *   <li>Resolves relative paths against a base resources path</li>
 *   <li>Allows absolute paths to bypass the base path</li>
 *   <li>Prevents path traversal attacks (e.g., "../../../etc/passwd")</li>
 *   <li>Normalizes paths for consistent behavior</li>
 * </ul>
 */
public class PathResolver {

    /**
     * Resolves a file path against a base resources path.
     *
     * <p>Resolution rules:</p>
     * <ul>
     *   <li>Absolute paths are used as-is (after normalization)</li>
     *   <li>Relative paths are resolved against the base resources path</li>
     *   <li>Path traversal attempts throw SecurityException</li>
     * </ul>
     *
     * <p><b>Examples:</b></p>
     * <pre>
     * resolvePath("data.json", "src/main/resources/")
     *   → "src/main/resources/data.json"
     *
     * resolvePath("/tmp/data.json", "src/main/resources/")
     *   → "/tmp/data.json" (absolute path bypasses base)
     *
     * resolvePath("../../../etc/passwd", "src/main/resources/")
     *   → SecurityException (path traversal blocked)
     * </pre>
     *
     * @param filePath the file path (relative or absolute)
     * @param resourcesPath the base resources path
     * @return the resolved absolute path
     * @throws IllegalArgumentException if filePath is null or empty
     * @throws SecurityException if path traversal is detected
     */
    public static String resolvePath(String filePath, String resourcesPath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        if (resourcesPath == null || resourcesPath.isEmpty()) {
            resourcesPath = JMiniAppConfig.DEFAULT_RESOURCES_PATH;
        }

        Path path = Paths.get(filePath);

        // If absolute, use as-is (but still normalize)
        if (path.isAbsolute()) {
            return path.normalize().toString();
        }

        // Resolve relative path against base
        Path basePath = Paths.get(resourcesPath);
        Path resolvedPath = basePath.resolve(filePath).normalize();

        // Security: ensure resolved path is within base path (prevent path traversal)
        Path normalizedBase = basePath.toAbsolutePath().normalize();
        Path normalizedResolved = resolvedPath.toAbsolutePath();

        if (!normalizedResolved.startsWith(normalizedBase)) {
            throw new SecurityException(
                "Path traversal detected: '" + filePath +
                "' resolves outside base path: '" + resourcesPath + "'"
            );
        }

        return resolvedPath.toString();
    }
}
