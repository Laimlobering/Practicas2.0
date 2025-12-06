package com.jminiapp.core.api;

/**
 * Abstract base class for all mini-apps.
 * 
 * <p>JMiniApp provides the template method pattern for mini-app lifecycle.
 * Subclasses must implement the lifecycle methods:</p>
 * <ul>
 *   <li>initialize() - Set up the app (load data, etc.)</li>
 *   <li>run() - Main application loop</li>
 *   <li>shutdown() - Clean up (save data, etc.)</li>
 * </ul>
 *
 * <p>The framework calls these methods in order through the start() method.</p>
 *
 * <p>Example mini-app implementation:</p>
 * <pre>
 * public class TodoApp extends JMiniApp {
 *     public TodoApp(String appName, MiniAppContext context) {
 *         super(appName, context);
 *     }
 *
 *     protected void initialize() {
 *         // Load existing todos
 *     }
 *
 *     protected void run() {
 *         // Show menu and handle user input
 *     }
 *
 *     protected void shutdown() {
 *         // Save todos
 *     }
 * }
 * </pre>
 */
public abstract class JMiniApp {
    // This context will be set by JMiniAppBuilder via setContext().
    protected JMiniAppContext context;
    protected String appName;
    
    /**
     * Create a new JMiniApp.
     * 
     * @param config the configuration object
     */
    public JMiniApp(JMiniAppConfig config) {
        this.appName = config.getAppName();        
    }

    /**
     * Start the mini-app.
     *
     * <p>This is the template method that orchestrates the lifecycle:
     * initialize → run → shutdown. 
     * 
     * Handles errors gracefully.</p>
     */
    public final void start() {
        try {
            initialize();
            run();
            shutdown();
        } catch (Exception e) {
            handleError(e);
        }
    }

    /**
     * Initialize the mini-app.
     *
     * <p>Called once before run(). Use this to:</p>
     * <ul>
     *   <li>Load existing data</li>
     *   <li>Set up resources</li>
     *   <li>Initialize state</li>
     * </ul>
     */
    protected abstract void initialize();

    /**
     * Run the main application loop.
     *
     * <p>This is where the main logic goes. Typically this will:</p>
     * <ul>
     *   <li>Display a menu</li>
     *   <li>Get user input</li>
     *   <li>Process commands</li>
     *   <li>Loop until user quits</li>
     * </ul>
     */
    protected abstract void run();

    /**
     * Shutdown the mini-app.
     *
     * <p>Called after run() completes. Use this to:</p>
     * <ul>
     *   <li>Save data</li>
     *   <li>Clean up resources</li>
     *   <li>Display goodbye message</li>
     * </ul>
     */
    protected abstract void shutdown();

     /**
     * Handle errors that occur during app execution.
     *
     * @param e the exception that occurred
     */
    protected void handleError(Exception e) {
        System.err.println("\nError in " + appName + ": " + e.getMessage());
        e.printStackTrace();
        System.err.println("\nThe application will now exit.");
    }

    /**
     * Get the app name.
     *
     * @return the app name
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Get the context.
     *
     * @return the mini-app context
     */
    public JMiniAppContext getContext() {
        return context;
    }

    /**
     * Set the context.
     *
     * <p>This method is called by the JMiniAppBuilder to inject the context
     * after the app is constructed.</p>
     *
     * @param context the mini-app context     
     */
    public void setContext(JMiniAppContext context) {
        this.context = context;
    }

}
