package com.example.notebook.controller;

import com.example.notebook.model.Note;
import com.example.notebook.service.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes")
    public String listNotes(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("activeNoteId", null);
        model.addAttribute("notes", noteService.findByUsername(userDetails.getUsername()));
        return "notes";
    }

    @GetMapping("/notes/{id}")
    public String listNotesWithNote(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        Note note = noteService.findById(id, userDetails.getUsername());
        model.addAttribute("activeNote", note);
        model.addAttribute("activeNoteId", note.getId());
        model.addAttribute("notes", noteService.findByUsername(userDetails.getUsername()));
        return "notes";
    }

    @GetMapping("/notes/new")
    public String getNewNote(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("activeNote", new Note());
        model.addAttribute("notes", noteService.findByUsername(userDetails.getUsername()));
        return "notes";
    }

    @PostMapping("/notes/new")
    public String saveNewNote(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam String title,
                              @RequestParam String content,
                              Model model) {
        noteService.addNote(userDetails.getUsername(), title, content);
        return "redirect:/notes";
    }

    @PostMapping("/notes/{id}")
    public String updateNote(@PathVariable Long id,
                            @RequestParam String title,
                            @RequestParam String content,
                            @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        noteService.updateNote(id, userDetails.getUsername(), title, content);
        return "redirect:/notes";
    }

    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        noteService.deleteById(id, userDetails.getUsername());
        return "redirect:/notes";
    }
}
