package com.example.michael.myapplication.models;

/**
 * Created by michael on 9/4/16.
 */
public class Photo {
    private String fileName;
    int noteId;

    public void setFileName (String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setNoteId (int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId() {
        return this.noteId;
    }
}
