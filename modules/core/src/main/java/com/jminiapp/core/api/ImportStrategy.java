package com.jminiapp.core.api;

import java.util.List;

/**
 * Strategy for merging imported data into the existing application state.
 * <p>Implementations define how to reconcile conflicts between current data and new data.</p>
 */
public interface ImportStrategy {
    /**
     * Merges imported items into the current list.
     *
     * @param currentData the mutable list of existing application data (can be modified)
     * @param importedData the list of new items being imported
     * @param <T> the type of data
     */
    <T> void merge(List<T> currentData, List<T> importedData);
}