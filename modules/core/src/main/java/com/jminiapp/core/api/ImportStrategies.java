package com.jminiapp.core.api;

import com.jminiapp.core.api.strategies.*;

public class ImportStrategies {
    // These behave like your old Enum values
    public static final ImportStrategy REPLACE = new ReplaceStrategy();
    public static final ImportStrategy APPEND = new AppendStrategy();
    public static final ImportStrategy SKIP_EXISTING = new SkipExistingStrategy();
    
    // Helper for the complex one
    public static ImportStrategy mergeById() {
        return new MergeByIdStrategy();
    }
}