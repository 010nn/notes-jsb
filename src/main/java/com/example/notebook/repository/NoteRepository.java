package com.example.notebook.repository;

import com.example.notebook.model.Note;
import com.example.notebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserOrderByTitleAsc(User user);
}
