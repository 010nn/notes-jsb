package com.example.notebook.controller;

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
}
