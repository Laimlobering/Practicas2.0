package com.jminiapp.examples.notePad;

import com.jminiapp.core.api.JMiniApp;
import com.jminiapp.core.api.JMiniAppConfig;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * A simple application that represents a notepad
 * 
 * The app allows user to:
 * - Create a new note
 * - Append text to the note
 * - Clear the note
 * - View the current note text
 * - Get the length of the note text
 */
public class NotePadApp extends JMiniApp {
    private Scanner scanner;
    private NotePadStates notePad;
    private boolean running;

    public NotePadApp(JMiniAppConfig config) {
        super(config);
    }

    @Override
    protected void initialize() {
        System.out.println("\n=== NotePad App ===");
        System.out.println("Welcome to the NotePad App");

        scanner = new Scanner(System.in);
        running = true;

        // Try to load existing note state from context
        List<NotePadStates> data = context.getData();
        if (data != null && !data.isEmpty()) {
            notePad = data.get(0);
            System.out.println("Loaded existing note: " + notePad.getNote());
        } else {
            notePad = new NotePadStates();
            System.out.println("Starting with a new empty note.");
        }
    }

    @Override
    protected void run() {
        while (running) {
            displayMenu();
            handleUserInput();
        }
    }

    @Override
    protected void shutdown() {
        // Save the note state to context
        List<NotePadStates> data = List.of(notePad);
        context.setData(data);

        scanner.close();
        System.out.println("\nFinal note text: " + notePad.getNote());
        System.out.println("Goodbye!");
    }

    private void displayMenu(){
        System.out.println("\n--- Current Note: " + notePad.getNote() + " ---");
        System.out.println("1. Append Text");
        System.out.println("2. Clear Note");
        System.out.println("3. Get Note Length");
        System.out.println("4. Export to JSON file");
        System.out.println("5. Import from JSON file");
        System.out.println("6. Exit");
        System.out.println("\nChoose an option: ");

    }

    private void handleUserInput() {
        try {
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("Enter text to append:");
                    String text = scanner.nextLine();
                    notePad.appendNote(text);
                    System.out.println("Append Note: " + text);
                    break;

                case "2":
                    notePad.clearNote();
                    System.out.println("Note cleared");
                    break;

                case "3":
                    int noteLength= notePad.getNoteLength();
                    System.out.println("Note length: " + noteLength);
                    break;

                case "4":
                    exportToFile();
                    break;

                case "5":
                    importFromFile();
                    break;

                case "6":
                    running = false;
                    System.out.println("\nExiting...");
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1-6.");
            }
        } catch (Exception e) {
            //I add this to see the full stack trace of any exception, to know what is happening
            System.out.println("Exception caught during command handling:");
            e.printStackTrace();
        }
    }

    //REVISAR
    private void exportToFile() {
        try {
            // Save current notePad to context before exporting
            context.setData(List.of(notePad));

            // Use default filename convention: {appName}.{format}
            context.exportData("json");
            System.out.println("NotePad state exported successfully to: NotePad.json");
        } catch (IOException e) {
            System.out.println("Error exporting file: " + e.getMessage());
        }
    }

    //REVISAR
    private void importFromFile() {
        try {

            // Use default filename convention: {appName}.{format}
            context.importData("json");

            // Update local counter from context after import
            List<NotePadStates> data = context.getData();
            if (data != null && !data.isEmpty()) {
                notePad = data.get(0);
                System.out.println("NotePad state imported successfully from NotePad.json!");
                System.out.println("New text: " + notePad.getNote());
            } else {
                System.out.println("Error: No data found in file.");
            }
        } catch (IOException e) {
             System.out.println("Import failed with exception:");
            e.printStackTrace();
        }
    }

}
