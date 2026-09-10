package com.example.notebook.model.dto;

import com.example.notebook.model.Note;

public class NoteDto {
    Long id;
    String title;
    String content;

    public NoteDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public NoteDto() {
    }

    public NoteDto(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
