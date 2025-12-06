package com.jminiapp.core.engine.internal;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jminiapp.core.engine.AppState;
import com.jminiapp.core.api.*;
import com.jminiapp.core.utils.*;

public class JMiniAppDefaultContext implements JMiniAppContext {
    private final AppState state;
    private final AdapterRegistry adapterRegistry;
    private final String appName;
    private final String resourcesPath;


    /**
     * Create a new MiniAppContextImpl with a model class (v2.0 constructor).
     *
     * <p>This constructor creates a new AppState and AdapterRegistry automatically,
     * suitable for standalone applications using the new bootstrap API.</p>
     *
     * @param appName the name of the mini-app
     * @param stateClass the model class for this application
     * @param resourcesPath base path for import/export file operations
     */
    public JMiniAppDefaultContext(String appName, Class<?> stateClass, String resourcesPath) {
        this.appName = appName;
        this.state = new AppState();
        this.adapterRegistry = new AdapterRegistry();
        this.resourcesPath = resourcesPath != null ? resourcesPath : JMiniAppConfig.DEFAULT_RESOURCES_PATH;
    }

    /**
     * Registers a format adapter with this context (v2.0 method).
     *
     * <p>This method allows direct registration of adapter instances,
     * used by the new discovery mechanisms.</p>
     *
     * @param adapter the adapter to register     
     */
    public void registerAdapter(JMiniFormatAdapter<?> adapter) {
        adapterRegistry.registerAdapter(appName, adapter);
    }

    @Override
    public <T> List<T> getData() {
        return state.getData();
    }

    /*
        @Override
    public <T> void setData(List<T> data) {
        state.setData(data);
    }
    */
    @Override
    public <T> void setData(List<T> data) {
        if (data == null) {
            // Guardar lista vacía mutable si nos pasan null
            state.setData(new ArrayList<>());
        } else {
            // Forzar copia mutable para evitar UnsupportedOperationException
            state.setData(new ArrayList<>(data));
        }
    }

    @Override
    public void clearData() {
        state.clear();
    }

    @Override
    public void importData(String format) throws IOException {
        importData(format, ImportStrategies.REPLACE);
    }

    @Override
    public void importData(String format, ImportStrategy strategy) throws IOException {
        String defaultFilename = buildDefaultFilename(format);
        importData(defaultFilename, format, strategy);
    }

    @Override
    public void importData(String filePath, String format) throws IOException {
        importData(filePath, format, ImportStrategies.REPLACE);
    }

    @Override
    public void importData(String filePath, String format, ImportStrategy strategy) throws IOException {
        JMiniFormatAdapter<?> adapter = getAdapterForFormat(format);

        String resolvedPath = PathResolver.resolvePath(filePath, resourcesPath);

        try (InputStream input = new FileInputStream(resolvedPath)) {
            List<?> importedData = adapter.read(input);
            mergeData(importedData, strategy);
        }
    }

    @Override
    public void exportData(String format) throws IOException {
        String defaultFilename = buildDefaultFilename(format);
        exportData(defaultFilename, format);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void exportData(String filePath, String format) throws IOException {
        @SuppressWarnings("rawtypes")
        JMiniFormatAdapter adapter = getAdapterForFormat(format);

        String resolvedPath = PathResolver.resolvePath(filePath, resourcesPath);

        // Create parent directories if they don't exist
        File file = new File(resolvedPath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (OutputStream output = new FileOutputStream(resolvedPath)) {
            adapter.write(getData(), output);
        }

        state.setModified(false);
    }

    @Override
    public List<String> getSupportedFormats() {
        return adapterRegistry.getSupportedFormats(appName);
    }

    @Override
    public boolean supportsFormat(String format) {
        return adapterRegistry.supportsFormat(appName, format);
    }

    @Override
    public String detectFormat(String filePath) {
        // Try extension-based detection first
        String extension = getFileExtension(filePath);
        if (extension != null && supportsFormat(extension)) {
            return extension;
        }

        // Try content-based detection for supported formats
        for (String format : getSupportedFormats()) {
            try {
                JMiniFormatAdapter<?> adapter = getAdapterForFormat(format);
                String resolvedPath = PathResolver.resolvePath(filePath, resourcesPath);
                try (InputStream input = new FileInputStream(resolvedPath)) {
                    if (adapter.validate(input)) {
                        return format;
                    }
                }
            } catch (Exception e) {
                // Continue to next format
            }
        }

        return null;
    }

    /**
     * Get an adapter for a specific format.
     *
     * @param format the format name
     * @return the adapter instance
     * @throws UnsupportedOperationException if format is not supported
     */
    private JMiniFormatAdapter<?> getAdapterForFormat(String format) {
        JMiniFormatAdapter<?> adapter = adapterRegistry.getAdapter(appName, format);

        if (adapter == null) {
            throw new UnsupportedOperationException(
                "Format '" + format + "' is not supported by " + appName +
                ". Supported formats: " + getSupportedFormats()
            );
        }

        return adapter;
    }

    /**
     * Merge imported data with existing data based on strategy.
     *
     * @param importedData the data to import
     * @param strategy the import strategy
    */

    /*
    @SuppressWarnings("unchecked")
    private void mergeData(List<?> importedData, ImportStrategy strategy) {        
        List<Object> currentData = (List<Object>) getData();
        List<Object> typedImportedData = (List<Object>) importedData;

        if (strategy != null) {
            strategy.merge(currentData, typedImportedData);
        } else {
             // Fallback if null passed, though ideally should not happen
             ImportStrategies.REPLACE.merge(currentData, typedImportedData);
        }

        state.setModified(true);
    }
    */

   @SuppressWarnings("unchecked")
    private void mergeData(List<?> importedData, ImportStrategy strategy) {
        // Normaliza nulls a listas vacías
        List<Object> currentDataRaw = (List<Object>) getData();
        List<Object> incomingRaw = (List<Object>) (importedData != null ? importedData : Collections.emptyList());

        // Asegurarnos de trabajar con listas mutables localmente
        List<Object> currentData = (currentDataRaw instanceof ArrayList)
                ? currentDataRaw
                : new ArrayList<>(currentDataRaw != null ? currentDataRaw : Collections.emptyList());

        List<Object> incomingData = new ArrayList<>(incomingRaw);

        // Si la estrategia es REPLACE -> haz la sustitución directa y segura
        if (strategy == ImportStrategies.REPLACE) {
            // Reemplazamos el estado con una copia mutable de los importados
            state.setData(new ArrayList<>(incomingData));
            state.setModified(true);
            return;
        }

        // Para otras estrategias: delegar en la estrategia pero sobre listas mutables
        if (strategy != null) {
            strategy.merge(currentData, incomingData);
        } else {
            // fallback seguro: replace semantics
            state.setData(new ArrayList<>(incomingData));
            state.setModified(true);
            return;
        }

        // Guardar el resultado de la merge (currentData puede haber cambiado)
        state.setData(currentData);
        state.setModified(true);
    }


    /**
     * Build a default filename based on app name and format.
     *
     * <p>Convention: {@code {appName}.{format}}</p>
     * <p>Example: "Counter.json", "TodoList.csv"</p>
     *
     * @param format the format extension
     * @return the default filename
     */
    private String buildDefaultFilename(String format) {
        return appName + "." + format;
    }

    /**
     * Get the file extension from a file path.
     *
     * @param filePath the file path
     * @return the extension (without dot), or null if none
     */
    private String getFileExtension(String filePath) {
        int lastDot = filePath.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filePath.length() - 1) {
            return filePath.substring(lastDot + 1).toLowerCase();
        }
        return null;
    }
}

