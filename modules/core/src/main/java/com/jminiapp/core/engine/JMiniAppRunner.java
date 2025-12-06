package com.jminiapp.core.engine;

import com.jminiapp.core.api.*;
import com.jminiapp.core.engine.internal.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unified entry point and builder for configuring and launching JMiniApp applications.
 *
 * <p><b>Example Usage:</b></p>
 * <pre>
 * public class CounterApp extends JMiniApp {
 * public static void main(String[] args) {
 * JMiniAppRunner.forApp(CounterApp.class)
 * .withState(CounterModel.class)
 * .withAdapters(new CSVAdapter(), new JSONAdapter())
 * .run(args);
 * }
 * }
 * </pre>
 */
public class JMiniAppRunner {

    // --- Configuration State ---
    private final Class<? extends JMiniApp> appClass;
    private Class<?> stateClass;
    private String appName;
    private final List<JMiniFormatAdapter<?>> adapters;
    private String resourcesPath = JMiniAppConfig.DEFAULT_RESOURCES_PATH;

    // --- Static Entry Points ---

    /**
     * Entry point: Creates a runner instance for the specified application class.
     *
     * @param appClass the MiniApp subclass to launch
     * @return a configurable runner instance
     */
    public static JMiniAppRunner forApp(Class<? extends JMiniApp> appClass) {
        if (appClass == null) {
            throw new IllegalArgumentException("App class cannot be null");
        }
        return new JMiniAppRunner(appClass);
    }

    /**
     * Convenience Entry point: Immediately runs the app with default settings.
     *
     * @param appClass the MiniApp subclass to launch
     * @param args command-line arguments
     */
    public static void run(Class<? extends JMiniApp> appClass, String[] args) {
        forApp(appClass).run(args);
    }

    // --- Constructor ---

    private JMiniAppRunner(Class<? extends JMiniApp> appClass) {
        this.appClass = appClass;
        this.adapters = new ArrayList<>();
    }

    // --- Fluent Builder Methods ---

    public JMiniAppRunner withState(Class<?> stateClass) {
        this.stateClass = stateClass;
        return this;
    }

    public JMiniAppRunner named(String appName) {
        this.appName = appName;
        return this;
    }

    public JMiniAppRunner withResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
        return this;
    }

    public JMiniAppRunner withAdapters(JMiniFormatAdapter<?>... adapters) {
        this.adapters.addAll(Arrays.asList(adapters));
        return this;
    }

    // --- Execution Logic ---

    /**
     * Builds the configuration, initializes the context, and starts the application.
     *
     * @param args command-line arguments
     */
    public void run(String[] args) {
        // 1. Build immutable config
        JMiniAppConfig config = buildConfiguration();

        // 2. Initialize Context
        JMiniAppContext context = createContext(config);

        // 3. Instantiate and Start App
        launchApp(config, context);
    }

    private JMiniAppConfig buildConfiguration() {
        String effectiveAppName = (appName != null) ? appName : appClass.getSimpleName();
        return new JMiniAppConfig(
            effectiveAppName,
            stateClass,
            appClass,
            new ArrayList<>(adapters),
            resourcesPath
        );
    }

    private JMiniAppContext createContext(JMiniAppConfig config) {
        JMiniAppDefaultContext context = new JMiniAppDefaultContext(
            config.getAppName(),
            config.getStateClass(),
            config.getResourcesPath()
        );

        for (JMiniFormatAdapter<?> adapter : config.getAdapters()) {
            context.registerAdapter(adapter);
        }
        return context;
    }

    private void launchApp(JMiniAppConfig config, JMiniAppContext context) {
        try {
            JMiniApp app = appClass.getDeclaredConstructor(JMiniAppConfig.class)
                    .newInstance(config);
            app.setContext(context);
            app.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch application: " + appClass.getName(), e);
        }
    }
}