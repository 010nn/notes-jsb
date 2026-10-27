package com.notesjsb.service;

import com.notesjsb.model.Note;
import com.notesjsb.model.User;
import com.notesjsb.model.dto.NoteDto;
import com.notesjsb.repository.NoteRepository;
import com.notesjsb.repository.UserRepository;
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

    public List<NoteDto> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<NoteDto> notesDto = noteRepository.findByUserOrderByTitleAsc(user).stream()
                .map(note -> new NoteDto(note))
                .toList();
        return notesDto;
    }

    public NoteDto findById(Long id, String username) {
        Note note = findByIdEntity(id, username);
        return new NoteDto(note);
    }

    public NoteDto createNote(String username, String title, String content) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUser(user);
        return new NoteDto(noteRepository.save(note));
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
