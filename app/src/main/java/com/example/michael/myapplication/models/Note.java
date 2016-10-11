package com.example.michael.myapplication.models;

import java.util.ArrayList;

/**
 * Created by michael on 9/4/16.
 */
public class Note {
    private int noteId, courseId;
    private String  noteContent, photo;

    public void setCourseId (int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setNoteContent (String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public void setPhotos(String photo) { this.photo = photo;}

    public String getPhoto() {
        return this.photo;
    }

    public void setNoteId (int noteId) {
        this.noteId = noteId;
    }

    public int getNoteId () {
        return this.noteId;
    }
}
