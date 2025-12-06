# NotePad Example

A command-line note-taking application demonstrating the JMiniApp framework.

## Overview

This example shows how to create an interactive note-taking application using JMiniApp core that manages a simple text note. Users can append text to the note, clear it, view its current content, check its length, and import/export the note state to JSON files.

## Features

- **Text Management**: Append new text to the current note or clear it completely.
- **Note Information**: View the current note content and check its length.
- **State Persistence**: Loads existing note state on startup and saves changes automatically when exiting.
- **JSON Import/Export**: Export the current note to a JSON file or import a previously saved note state.

## Project Structure

```
notePad/
├── pom.xml
├── README.md
└── src/main/java/com/jminiapp/examples/notePad/
    ├── NotePadApp.java          # Main application class
    ├── NotePadAppRunner.java    # Bootstrap configuration
    ├── NotePadStates.java       # Note model class
    └── NotePadJSONAdapter.java  # JSON format adapter
```
## Key Components

### NotePadStates
A simple model class that represents a note with:
- `note`: The text content of the note.
- `appendNote()`: Method to append additional text to the current note.
- `clearNote()`: Method to clear the note content.
- `getNoteLength`: Method to get the length of the current note text.
- `getNote()`: Method to retrieve the current note content.

### NotePadJSONAdapter
A format adapter that enables JSON import/export for `NotePadStates` objects:
- Implements `JSONAdapter<NotePadStates>` from the framework.
- Registers with the framework during app bootstrap.
- Provides automatic serialization/deserialization for the note state.

### NotePadApp
The main application class that extends `JMiniApp` and implements:
- `initialize()`: Loads existing note state from `NotePad.json` automatically.
- `run()`: Main loop displaying the interactive menu with current note preview.
- `shutdown()`: Saves the note state to `NotePad.json` before exiting.
- `handleUserInput()`: Processes user commands for all note operations.

### NotePadAppRunner
Bootstrap configuration that:
- Registers the `NotePadJSONAdapter ` with `.withAdapters()`.
- Configures the app name and model class (`NotePadStates.class`).
- Launches the application.

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build the project

From the **project root** (not the examples/notePad directory):
```bash
mvn clean install
```
This will build both the jminiapp-core module and the notePad example.

### Run the application

Option 1: Using Maven exec plugin (from the examples/notePad directory)
```bash
cd examples/notePad
mvn exec:java
```
Option 2: Using the packaged JAR (from the examples/notePad directory)
```bash
cd examples/notePad
java -jar target/notePad-example.jar
```
Option 3: From the project root
```bash
cd examples/notePad && mvn exec:java
```
### Usage Example

Starting the app with a new note:
```
=== NotePad App ===
Welcome to the NotePad App
Starting with a new empty note.

--- Current Note:  ---
1. Append Text
2. Clear Note
3. Get Note Length
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 1
Enter text to append:
Hello, this is my first note!
Append Note: Hello, this is my first note!

```
Appending more text and checking length:

```
--- Current Note: Hello, this is my first note! ---
1. Append Text
2. Clear Note
3. Get Note Length
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 1
Enter text to append: 
Adding more important information.
Append Note:  Adding more important information.

--- Current Note: Hello, this is my first note! Adding more important information. ---
1. Append Text
2. Clear Note
3. Get Note Length
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 3
Note length: 57

```

Exporting and Importing note state:

```
--- Current Note: Hello, this is my first note! Adding more important information. ---
1. Append Text
2. Clear Note
3. Get Note Length
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 4
NotePad state exported successfully to: NotePad.json

Choose an option: 2
Note cleared

--- Current Note:  ---
1. Append Text
2. Clear Note
3. Get Note Length
4. Export to JSON file
5. Import from JSON file
6. Exit

Choose an option: 5
NotePad state imported successfully from NotePad.json!
New text: Hello, this is my first note! Adding more important information.

```
The JSON file format for NotePad states is simple:

```
[
  {
    "note": "Hello, this is my first note! Adding more important information."
  }
]

```

Author: [Jesús Alberto Aviles Castillo / Laimlobering]