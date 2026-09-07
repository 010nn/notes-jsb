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
        model.addAttribute("notes", noteService.findByUsername(userDetails.getUsername()));
        return "notes";
    }

    @GetMapping("/notes/new")
    public String newNoteForm(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("note", new Note());
        model.addAttribute("allNotes", noteService.findByUsername(userDetails.getUsername()));
        return "note-form";
    }

    @PostMapping("/notes")
    public String saveNote(@RequestParam String title, 
                          @RequestParam String content,
                          @AuthenticationPrincipal UserDetails userDetails,
                          RedirectAttributes redirectAttributes) {
        noteService.save(userDetails.getUsername(), title, content);
        redirectAttributes.addFlashAttribute("success", "Note created successfully");
        return "redirect:/notes";
    }

    @GetMapping("/notes/{id}/edit")
    public String editNoteForm(@PathVariable Long id, 
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        Note note = noteService.findById(id, userDetails.getUsername());
        model.addAttribute("note", note);
        model.addAttribute("allNotes", noteService.findByUsername(userDetails.getUsername()));
        return "note-form";
    }

    @PostMapping("/notes/{id}")
    public String updateNote(@PathVariable Long id,
                            @RequestParam String title,
                            @RequestParam String content,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        Note note = noteService.findById(id, userDetails.getUsername());
        note.setTitle(title);
        note.setContent(content);
        noteService.save(userDetails.getUsername(), title, content);
        redirectAttributes.addFlashAttribute("success", "Note updated successfully");
        return "redirect:/notes";
    }

    @PostMapping("/notes/{id}/delete")
    public String deleteNote(@PathVariable Long id,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        noteService.deleteById(id, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("success", "Note deleted successfully");
        return "redirect:/notes";
    }
}
