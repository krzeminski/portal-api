package com.example.portalapi.controller;

import com.example.portalapi.entity.dto.NoteDTO;
import com.example.portalapi.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }
    @GetMapping("/notes")
    Page<NoteDTO> getNotes(@PageableDefault(size = 5) Pageable paging){
        return noteService.getNotesWithUsername(paging);
    }
}
