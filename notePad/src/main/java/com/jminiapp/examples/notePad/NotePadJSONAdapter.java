package com.jminiapp.examples.notePad;

import com.jminiapp.core.adapters.JSONAdapter;

/**
 * JSON adapter for CounterState objects.
 *
 * <p>This adapter enables the NotePad app to import and export notepad state
 * to/from JSON files. It leverages the framework's JSONAdapter interface which
 * provides default implementations for serialization using Gson.</p>
 *
 * <p>Example JSON format:</p>
 * <pre>
 * [
 *   {
 *     "note": Pongame 10 Profe
 *   }
 * ]
 * </pre>
 */
public class NotePadJSONAdapter implements JSONAdapter<NotePadStates>{
    @Override
    public Class<NotePadStates> getstateClass() {
        return NotePadStates.class;
    }
}
