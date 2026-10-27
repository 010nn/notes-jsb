package com.notesjsb.repository;

import com.notesjsb.model.Note;
import com.notesjsb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserOrderByTitleAsc(User user);
}
