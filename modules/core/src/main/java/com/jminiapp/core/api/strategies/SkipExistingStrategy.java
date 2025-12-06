package com.jminiapp.core.api.strategies;

import java.util.List;

import com.jminiapp.core.api.ImportStrategy;

public class SkipExistingStrategy implements ImportStrategy {
    @Override
    public <T> void merge(List<T> currentData, List<T> importedData) {
        for (T item : importedData) {
            if (!currentData.contains(item)) {
                currentData.add(item);
            }
        }
    }
}