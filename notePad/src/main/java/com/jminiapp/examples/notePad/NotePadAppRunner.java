package com.jminiapp.examples.notePad;

import com.jminiapp.core.engine.JMiniAppRunner;

public class NotePadAppRunner {
    public static void main(String[] args) {
        JMiniAppRunner
            .forApp(NotePadApp.class)
            .withState(NotePadStates.class)
            .withAdapters(new NotePadJSONAdapter())
            //.withResourcesPath("test-data/")  // Custom import/export path
            .named("NotePad")
            .run(args);
    }
}
