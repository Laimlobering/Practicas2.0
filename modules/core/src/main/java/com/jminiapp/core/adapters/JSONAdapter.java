package com.jminiapp.core.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jminiapp.core.api.JMiniFormatAdapter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON-specific adapter interface with default implementations using Gson.
 *
 * <p>This interface extends JMiniFormatAdapter and provides JSON-specific methods
 * for converting individual objects to/from JSON strings. It includes default
 * implementations of read() and write() that use the Gson library.</p>
 *
 * <p>Implementers only need to provide:</p>
 * <ul>
 *   <li>getstateClass() - Return the class type of objects</li>
 * </ul>
 *
 * <p>Optional overrides:</p>
 * <ul>
 *   <li>toJSON() - Custom JSON serialization</li>
 *   <li>fromJSON() - Custom JSON deserialization</li>
 *   <li>configureGson() - Customize Gson configuration</li>
 * </ul>
 *
 * @param <T> the type of objects this adapter handles
 */
public interface JSONAdapter<T> extends JMiniFormatAdapter<T> {

    /**
     * Convert a single object to a JSON string.
     *
     * <p>Default implementation uses Gson with pretty printing.</p>
     *
     * @param object the object to convert
     * @return JSON string representation
     */
    default String toJSON(T object) {
        return configureGson().toJson(object);
    }

    /**
     * Convert a JSON string to an object.
     *
     * <p>Default implementation uses Gson to parse the JSON.</p>
     *
     * @param json the JSON string to parse
     * @return the reconstructed object
     */
    default T fromJSON(String json) {
        return configureGson().fromJson(json, getstateClass());
    }

    /**
     * Get the model class type for this adapter.
     *
     * <p>This is required for Gson to properly deserialize objects.</p>
     *
     * @return the class object for type T
     */
    Class<T> getstateClass();

    /**
     * Configure the Gson instance used for serialization/deserialization.
     *
     * <p>Default configuration includes:</p>
     * <ul>
     *   <li>Pretty printing enabled</li>
     *   <li>Date format: yyyy-MM-dd</li>
     * </ul>
     *
     * <p>Override this method to customize Gson behavior.</p>
     *
     * @return configured Gson instance
     */
    default Gson configureGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd")
                .create();
    }

    /**
     * Get the format name for JSON.
     *
     * @return "json"
     */
    @Override
    default String getFormatName() {
        return "json";
    }

    /**
     * Read JSON data from an input stream.
     *
     * <p>Default implementation uses Gson to parse a JSON array.</p>
     *
     * @param input the input stream containing JSON data
     * @return list of objects parsed from JSON
     * @throws IOException if an I/O error occurs
     */
    @Override
    default List<T> read(InputStream input) throws IOException {
        try (Reader reader = new InputStreamReader(input)) {
            Gson gson = configureGson();
            Type listType = TypeToken.getParameterized(List.class, getstateClass()).getType();
            List<T> result = gson.fromJson(reader, listType);
            return result != null ? result : new java.util.ArrayList<>();
        }
    }

    /**
     * Write objects to JSON format.
     *
     * <p>Default implementation uses Gson to write a JSON array.</p>
     *
     * @param data the list of objects to write
     * @param output the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    @Override
    default void write(List<T> data, OutputStream output) throws IOException {
        Gson gson = configureGson();
        String json = gson.toJson(data);
        try (Writer writer = new OutputStreamWriter(output)) {
            writer.write(json);
        }
    }
}
