package com.example.notebook.service;

import com.example.notebook.model.Note;
import com.example.notebook.model.User;
import com.example.notebook.model.dto.NoteDto;
import com.example.notebook.repository.NoteRepository;
import com.example.notebook.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    private Note findByIdEntity(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        if (!note.getUser().equals(user)) {
            throw new RuntimeException("Access denied");
        }
        return note;
    }

    public List<Note> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return noteRepository.findByUserOrderByTitleAsc(user);
    }

    public NoteDto findById(Long id, String username) {
        Note note = findByIdEntity(id, username);
        return new NoteDto(note);
    }

    public Note addNote(String username, String title, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUser(user);
        return noteRepository.save(note);
    }

    public void updateNote(Long id, String username, String title, String content) {
        Note note = findByIdEntity(id, username);
        note.setContent(content);
        note.setTitle(title);
        noteRepository.save(note);
    }

    public void deleteById(Long id, String username) {
        Note note = findByIdEntity(id, username);
        noteRepository.delete(note);
    }
}
