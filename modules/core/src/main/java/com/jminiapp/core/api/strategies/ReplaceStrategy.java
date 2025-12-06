package com.jminiapp.core.api.strategies;

import java.util.ArrayList;
import java.util.List;

import com.jminiapp.core.api.ImportStrategy;

public class ReplaceStrategy implements ImportStrategy {
    /*
    @Override
    public <T> void merge(List<T> currentData, List<T> importedData) {
        currentData.clear();
        currentData.addAll(importedData);
    }
    */

    @Override
    public <T> void merge(List<T> currentData, List<T> importedData) {

        // Siempre convertir importedData en una lista MUTABLE
        List<T> mutableImported = new ArrayList<>(importedData);

        // Y también garantizar que currentData sea mutable
        List<T> mutableCurrent;
        try {
            currentData.clear();         // Si esto falla, la lista es inmutable
            currentData.addAll(mutableImported);
            return; // Done
        } catch (UnsupportedOperationException ex) {
            // La currentData era inmutable → crear nueva mutable
            mutableCurrent = new ArrayList<>();
            mutableCurrent.addAll(mutableImported);
        }

        // Regresar la lista mutable al contexto.  
        // PEROOO... ReplaceStrategy NO tiene acceso al contexto.
        // Así que solo hacemos esto:
        currentData = mutableCurrent;
    }


}