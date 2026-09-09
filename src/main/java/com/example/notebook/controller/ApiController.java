package com.example.notebook.controller;

import com.example.notebook.model.dto.NoteDto;
import com.example.notebook.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ApiController {

    NoteService noteService;

    public ApiController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteDto> getNote(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        NoteDto note = noteService.findById(id, userDetails.getUsername());
        return ResponseEntity.ok(note);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Void> updateNote(@PathVariable Long id, @RequestBody NoteDto dto,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        noteService.updateNote(id, userDetails.getUsername(), dto.getTitle(), dto.getContent());
        return ResponseEntity.ok().build();
    }
}
