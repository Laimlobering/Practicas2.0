package com.jminiapp.core.api;

import java.util.*;

/**
 * Internal configuration object for mini-applications.
 *
 * <p>This class encapsulates all configuration parameters needed to bootstrap
 * and run a mini-application. It is created by {@link com.jminiapp.framework.JMiniAppBuilder}
 * and passed to the MiniApp constructor.</p>
 *
 * <p>This class is immutable to prevent configuration changes after application startup.</p>
 */
public class JMiniAppConfig {

    /**
     * Default base path for import/export file operations.
     * Follows Maven standard directory structure.
     */
    public static final String DEFAULT_RESOURCES_PATH = "src/main/resources/";

    private final String appName;
    private final Class<?> stateClass;
    private final Class<? extends JMiniApp> appClass;
    private final List<JMiniFormatAdapter<?>> adapters;
    private final String resourcesPath;

    /**
     * Creates a new configuration instance.
     *
     * @param appName the application name
     * @param stateClass the model class (e.g., TodoItem.class)
     * @param appClass the MiniApp subclass
     * @param adapters list of programmatically registered adapters
     * @param resourcesPath base path for import/export file operations
     */
    public JMiniAppConfig(
        String appName,
        Class<?> stateClass,
        Class<? extends JMiniApp> appClass,
        List<JMiniFormatAdapter<?>> adapters,
        String resourcesPath
    ) {

        this.appName = appName;
        this.stateClass = stateClass;
        this.appClass = appClass;
        this.adapters = Collections.unmodifiableList(adapters);
        this.resourcesPath = resourcesPath != null ? resourcesPath : DEFAULT_RESOURCES_PATH;
    }

     /**
     * Returns the application name.
     *
     * @return the application name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Returns the model class for this application.
     *
     * @return the model class
     */
    public Class<?> getStateClass() {
        return stateClass;
    }

    /**
     * Returns the MiniApp subclass.
     *
     * @return the app class
     */
    public Class<? extends JMiniApp> getAppClass() {
        return appClass;
    }

    /**
     * Returns the list of programmatically registered adapters.
     *
     * @return unmodifiable list of adapters
     */
    public List<JMiniFormatAdapter<?>> getAdapters() {
        return adapters;
    }

    /**
     * Returns the base path for import/export file operations.
     *
     * <p>Relative file paths in importData/exportData will be resolved
     * against this base path. Defaults to "src/main/resources/".</p>
     *
     * @return the resources path
     */
    public String getResourcesPath() {
        return resourcesPath;
    }
}
