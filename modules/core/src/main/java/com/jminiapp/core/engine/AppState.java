package com.jminiapp.core.engine;

import java.util.*;

/**
 * Container for application state (data).
 *
 * <p>AppState holds the current state of a mini-app, including:</p>
 * <ul>
 *   <li>The actual data (typically a List of model objects)</li>
 *   <li>Modification tracking</li>
 *   <li>Model class type information</li>
 * </ul>
 *
 * <p>This class provides type-safe access to the application data and
 * tracks whether the data has been modified since the last save.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * AppState state = new AppState(TodoItem.class);
 * List&lt;TodoItem&gt; todos = state.getData();
 * todos.add(new TodoItem("Buy milk"));
 * state.setData(todos);
 * state.setModified(true);
 * </pre>
 */
public class AppState {

    private Object data;
    private boolean modified;
    private Class<?> stateClass;

    /**
     * Create a new empty AppState.
     */
    public AppState() {
        this.data = new ArrayList<>();
        this.modified = false;
    }

    /**
     * Create a new AppState for a specific model class.
     *
     * @param stateClass the class of objects stored in this state
     */
    public AppState(Class<?> stateClass) {
        this();
        this.stateClass = stateClass;
    }

    /**
     * Get the data from this state.
     *
     * <p>Returns the data as a List. If no data has been set,
     * returns an empty list.</p>
     *
     * @param <T> the type of objects in the list
     * @return the current data as a List
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getData() {
        if (data == null) {
            data = new ArrayList<T>();
        }
        return (List<T>) data;
    }

    /**
     * Set the data for this state.
     *
     * @param data the new data
     * @param <T> the type of objects in the list
     */
    public <T> void setData(List<T> data) {
        this.data = data;
        this.modified = true;
    }

    /**
     * Clear all data from this state.
     */
    public void clear() {
        if (data != null) {
            getData().clear();
            this.modified = true;
        }
    }

    /**
     * Check if the data has been modified.
     *
     * @return true if data has been modified since last save
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Set the modified flag.
     *
     * @param modified true if data has been modified
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }

    /**
     * Get the model class for this state.
     *
     * @return the class of objects stored in this state
     */
    public Class<?> getstateClass() {
        return stateClass;
    }

    /**
     * Set the model class for this state.
     *
     * @param stateClass the class of objects stored in this state
     */
    public void setstateClass(Class<?> stateClass) {
        this.stateClass = stateClass;
    }

    /**
     * Get the number of items in the state.
     *
     * @return the count of items
     */
    public int size() {
        return getData().size();
    }

    /**
     * Check if the state is empty.
     *
     * @return true if there are no items in the state
     */
    public boolean isEmpty() {
        return getData().isEmpty();
    }
}
