package com.jminiapp.core.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Base interface for all format adapters.
 *
 * <p>FormatAdapter defines the contract for converting between application data
 * and various file formats (CSV, JSON, XML, HTML, etc.).</p>
 *
 * <p>Each adapter implementation must provide:</p>
 * <ul>
 *   <li>read() - Convert from format to application objects</li>
 *   <li>write() - Convert from application objects to format</li>
 *   <li>getFormatName() - Return the format identifier (e.g., "csv", "json")</li>
 * </ul>
 *
 * @param <T> the type of objects this adapter handles
 */
public interface JMiniFormatAdapter<T> {

    /**
     * Read data from an input stream and convert to application objects.
     *
     * @param input the input stream containing data in this format
     * @return list of objects read from the stream
     * @throws IOException if an I/O error occurs during reading
     */
    List<T> read(InputStream input) throws IOException;

    /**
     * Write application objects to an output stream in this format.
     *
     * @param data the list of objects to write
     * @param output the output stream to write to
     * @throws IOException if an I/O error occurs during writing
     */
    void write(List<T> data, OutputStream output) throws IOException;

    /**
     * Get the name identifier for this format.
     *
     * @return the format name (e.g., "csv", "json", "xml")
     */
    String getFormatName();

    /**
     * Validate that the input stream contains valid data for this format.
     *
     * <p>Default implementation attempts to read the stream and returns true
     * if successful, false otherwise.</p>
     *
     * @param input the input stream to validate
     * @return true if the stream contains valid data for this format
     */
    default boolean validate(InputStream input) {
        try {
            read(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
