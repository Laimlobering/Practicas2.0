package com.jminiapp.core.api.strategies;

import java.lang.reflect.Method;
import java.util.List;

import com.jminiapp.core.api.ImportStrategy;

public class MergeByIdStrategy implements ImportStrategy {
    
    private final String idFieldName;

    /**
     * Default constructor uses "getId".
     */
    public MergeByIdStrategy() {
        this("getId");
    }

    /**
     * Allow users to specify a different ID getter method (e.g., "getUuid").
     */
    public MergeByIdStrategy(String idGetterName) {
        this.idFieldName = idGetterName;
    }

    @Override
    public <T> void merge(List<T> currentData, List<T> importedData) {
        if (importedData.isEmpty()) return;

        try {
            // Reflection setup
            T firstItem = importedData.get(0);
            Method getIdMethod = firstItem.getClass().getMethod(idFieldName);

            for (T newItem : importedData) {
                Object newId = getIdMethod.invoke(newItem);
                boolean found = false;

                // Scan existing data
                for (int i = 0; i < currentData.size(); i++) {
                    T existingItem = currentData.get(i);
                    Object existingId = getIdMethod.invoke(existingItem);

                    if (newId.equals(existingId)) {
                        currentData.set(i, newItem); // Update/Replace
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    currentData.add(newItem); // Insert
                }
            }
        } catch (Exception e) {
            // Fallback: If we can't find IDs, just append and warn
            System.err.println("MergeById failed: " + e.getMessage() + ". Appending instead.");
            currentData.addAll(importedData);
        }
    }
}