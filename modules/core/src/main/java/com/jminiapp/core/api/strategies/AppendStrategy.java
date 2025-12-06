package com.jminiapp.core.api.strategies;

import java.util.List;

import com.jminiapp.core.api.ImportStrategy;

public class AppendStrategy implements ImportStrategy {
    @Override
    public <T> void merge(List<T> currentData, List<T> importedData) {
        currentData.addAll(importedData);
    }
}