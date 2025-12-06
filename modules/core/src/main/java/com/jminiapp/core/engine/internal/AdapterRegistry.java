package com.jminiapp.core.engine.internal;

import java.util.*;

import com.jminiapp.core.api.JMiniFormatAdapter;

/**
 * Registry for managing format adapters for mini-apps.
 *
 * <p>AdapterRegistry maintains a mapping of mini-apps to their supported
 * format adapters. It provides:</p>
 * <ul>
 *   <li>Registration of adapters by app name and format</li>
 *   <li>Adapter lookup by app name and format</li>
 *   <li>Query of supported formats per app</li>
 *   <li>Adapter instantiation via reflection</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>
 * AdapterRegistry registry = new AdapterRegistry();
 * registry.registerAdapter("TodoList", "csv",
 *     "com.jminiapp.examples.todo.adapters.TodoCSVAdapter");
 *
 * FormatAdapter adapter = registry.getAdapter("TodoList", "csv");
 * </pre>
 */
public class AdapterRegistry {

    private final Map<String, Map<String, JMiniFormatAdapter<?>>> adapters;

    /**
     * Create a new AdapterRegistry.
     */
    public AdapterRegistry() {
        this.adapters = new HashMap<>();
    }

    /**
     * Register an adapter for a mini-app and format.
     *
     * <p>The adapter is instantiated using reflection from the provided
     * class name. The class must have a no-argument constructor.</p>
     *
     * @param appName the name of the mini-app
     * @param format the format name (e.g., "csv", "json")
     * @param adapterClassName the fully qualified adapter class name
     * @throws IllegalArgumentException if the adapter class cannot be instantiated
     */
    public void registerAdapter(String appName, String format, String adapterClassName) {
        JMiniFormatAdapter<?> adapter = instantiateAdapter(adapterClassName);

        adapters.computeIfAbsent(appName, k -> new HashMap<>())
                .put(format.toLowerCase(), adapter);
    }

    /**
     * Register an adapter instance directly.
     *
     * <p>This is useful for testing or when you already have an adapter instance.</p>
     *
     * @param appName the name of the mini-app
     * @param format the format name
     * @param adapter the adapter instance
     */
    public void registerAdapter(String appName, String format, JMiniFormatAdapter<?> adapter) {
        adapters.computeIfAbsent(appName, k -> new HashMap<>())
                .put(format.toLowerCase(), adapter);
    }

    /**
     * Register an adapter instance with auto-detected format (v2.0 method).
     *
     * <p>This method auto-detects the format from the adapter's getFormatName() method.
     * Used by the new discovery mechanisms in v2.0.</p>
     *
     * @param appName the name of the mini-app
     * @param adapter the adapter instance     
     */
    public void registerAdapter(String appName, JMiniFormatAdapter<?> adapter) {
        String format = adapter.getFormatName();
        registerAdapter(appName, format, adapter);
    }

    /**
     * Get an adapter for a specific app and format.
     *
     * @param appName the name of the mini-app
     * @param format the format name
     * @return the adapter instance, or null if not found
     */
    public JMiniFormatAdapter<?> getAdapter(String appName, String format) {
        Map<String, JMiniFormatAdapter<?>> appAdapters = adapters.get(appName);
        if (appAdapters == null) {
            return null;
        }
        return appAdapters.get(format.toLowerCase());
    }

    /**
     * Get all supported formats for a mini-app.
     *
     * @param appName the name of the mini-app
     * @return list of supported format names
     */
    public List<String> getSupportedFormats(String appName) {
        Map<String, JMiniFormatAdapter<?>> appAdapters = adapters.get(appName);
        if (appAdapters == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(appAdapters.keySet());
    }

    /**
     * Check if a format is supported by a mini-app.
     *
     * @param appName the name of the mini-app
     * @param format the format name to check
     * @return true if the format is supported
     */
    public boolean supportsFormat(String appName, String format) {
        Map<String, JMiniFormatAdapter<?>> appAdapters = adapters.get(appName);
        if (appAdapters == null) {
            return false;
        }
        return appAdapters.containsKey(format.toLowerCase());
    }

    /**
     * Get all registered app names.
     *
     * @return set of app names
     */
    public Set<String> getRegisteredApps() {
        return adapters.keySet();
    }

    /**
     * Clear all registered adapters for a mini-app.
     *
     * @param appName the name of the mini-app
     */
    public void clearAdapters(String appName) {
        adapters.remove(appName);
    }

    /**
     * Clear all registered adapters.
     */
    public void clearAll() {
        adapters.clear();
    }

    /**
     * Instantiate an adapter from a class name using reflection.
     *
     * @param className the fully qualified class name
     * @return the adapter instance
     * @throws IllegalArgumentException if the class cannot be instantiated
     */
    private JMiniFormatAdapter<?> instantiateAdapter(String className) {
        try {
            Class<?> adapterClass = Class.forName(className);
            Object instance = adapterClass.getDeclaredConstructor().newInstance();

            if (!(instance instanceof JMiniFormatAdapter)) {
                throw new IllegalArgumentException(
                    "Class " + className + " does not implement JMiniFormatAdapter"
                );
            }

            return (JMiniFormatAdapter<?>) instance;

        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                "Adapter class not found: " + className, e
            );
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "Adapter class " + className + " must have a no-argument constructor", e
            );
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Failed to instantiate adapter: " + className, e
            );
        }
    }
}
