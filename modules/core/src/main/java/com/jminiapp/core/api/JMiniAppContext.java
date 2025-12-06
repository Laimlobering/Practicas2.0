package com.jminiapp.core.api;

import java.io.IOException;
import java.util.*;

/**
 * Context API for mini-apps to interact with framework services. 
 */
public interface JMiniAppContext {
    /**
     * Get the current application data.
     *
     * @param <T> the type of objects in the list
     * @return the current data as a list
     */
    <T> List<T> getData();

    /**
     * Set the application data.
     *
     * @param data the new data
     * @param <T> the type of objects in the list
     */
    <T> void setData(List<T> data);

    /**
     * Clear all application data.
     */
    void clearData();

    /**
     * Import data from the default file using the specified format.
     *
     * <p>Uses convention-based filename: {@code {appName}.{format}}</p>
     * <p>Uses REPLACE strategy by default (replaces all existing data).</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Imports from "Counter.json" (for app named "Counter")
     * context.importData("json");
     * </pre>
     *
     * @param format the format name (e.g., "csv", "json", "xml")
     * @throws IOException if file cannot be read or format is invalid
     * @throws UnsupportedOperationException if format is not supported
     */
    void importData(String format) throws IOException;

    /**
     * Import data from a file using the specified format.
     *
     * <p>Uses REPLACE strategy by default (replaces all existing data).</p>
     *
     * @param filePath path to the file to import
     * @param format the format name (e.g., "csv", "json", "xml")
     * @throws IOException if file cannot be read or format is invalid
     * @throws UnsupportedOperationException if format is not supported
     */
    void importData(String filePath, String format) throws IOException;

    /**
     * Import data from the default file using the specified format and import strategy.
     *
     * <p>Uses convention-based filename: {@code {appName}.{format}}</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Imports from "Counter.json" using APPEND strategy
     * context.importData("json", ImportStrategy.APPEND);
     * </pre>
     *
     * @param format the format name (e.g., "csv", "json", "xml")
     * @param strategy how to handle existing data
     * @throws IOException if file cannot be read or format is invalid
     * @throws UnsupportedOperationException if format is not supported     
     */
    void importData(String format, ImportStrategy strategy) throws IOException;

    /**
     * Import data from a file using the specified format and import strategy.
     *
     * @param filePath path to the file to import
     * @param format the format name (e.g., "csv", "json", "xml")
     * @param strategy how to handle existing data
     * @throws IOException if file cannot be read or format is invalid
     * @throws UnsupportedOperationException if format is not supported
     */
    void importData(String filePath, String format, ImportStrategy strategy) throws IOException;

    /**
     * Export data to the default file in the specified format.
     *
     * <p>Uses convention-based filename: {@code {appName}.{format}}</p>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Exports to "Counter.json" (for app named "Counter")
     * context.exportData("json");
     * </pre>
     *
     * @param format the format name (e.g., "csv", "json", "xml", "html")
     * @throws IOException if file cannot be written
     * @throws UnsupportedOperationException if format is not supported     
     */
    void exportData(String format) throws IOException;

    /**
     * Export data to a file in the specified format.
     *
     * @param filePath path to the file to create
     * @param format the format name (e.g., "csv", "json", "xml", "html")
     * @throws IOException if file cannot be written
     * @throws UnsupportedOperationException if format is not supported
     */
    void exportData(String filePath, String format) throws IOException;
    
    /**
     * Get the list of supported format names for this mini-app.
     *
     * @return list of format names (e.g., ["csv", "json", "xml"])
     */
    List<String> getSupportedFormats();

    /**
     * Check if a specific format is supported by this mini-app.
     *
     * @param format the format name to check
     * @return true if the format is supported
     */
    boolean supportsFormat(String format);

    /**
     * Detect the format of a file.
     *
     * <p>Attempts to detect format based on file extension first,
     * then falls back to content analysis if needed.</p>
     *
     * @param filePath path to the file to analyze
     * @return the detected format name, or null if cannot be detected
     */
    String detectFormat(String filePath);
}