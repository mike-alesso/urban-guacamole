package com.example.michael.myapplication.models;

/**
 * Created by michael on 9/4/16.
 */
public class Note {
    private int noteId;
    private String courseName, noteContent;

    public void setCourseName (String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public void setNoteContent (String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public int getNoteId () {
        return this.noteId;
    }
}
