package com.example.notebook.controller;

import com.example.notebook.model.Note;
import com.example.notebook.model.dto.NoteDto;
import com.example.notebook.service.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping()
    public String listNotes(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("activeNoteId", null);
        model.addAttribute("notes", noteService.findByUsername(userDetails.getUsername()));
        return "notes";
    }

    @GetMapping("/new")
    public String getNewNote(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("activeNote", new NoteDto());
        model.addAttribute("notes", noteService.findByUsername(userDetails.getUsername()));
        return "notes";
    }

    @PostMapping("/{id}/delete")
    public String deleteNote(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        noteService.deleteById(id, userDetails.getUsername());
        return "redirect:/notes";
    }
}
