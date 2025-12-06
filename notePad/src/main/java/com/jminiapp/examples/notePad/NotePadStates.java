package com.jminiapp.examples.notePad;


public class NotePadStates {
    private String note;

    /**
     * Create a new empty note
     */
    public NotePadStates() {
        this.note = "";
    }

    /**
     * Create a new note with a specific text
     * @param note the specific text
     */
    public NotePadStates(String note) {
        this.note = note;
    }


    /**
     * Get the current note text
     * @return the current text
     */
    public String getNote() {
        return note;
    }

    /**
     * Set the note text
     * @param note the new text
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Append text to the current note
     * @param additionalText the new text to append
     * @return the new note text
     */
    public String appendNote(String additionalText) {
        if (additionalText == null) return this.note;
        return this.note += additionalText;
    }

    /**
     * Clear the note text
     * @return the cleared note text
     */
    public String clearNote() {
        return this.note = "";
    }

    /**
     * Get the lenght of the note text
     * @return the length of the note text
     */
    public int getNoteLength() {
        return this.note.length();
    }

    @Override
    public String toString() {
        return "NotePadStates{note='" + note + "'}";
    }

}
