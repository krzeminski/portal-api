package com.example.portalapi.controller;

import com.example.portalapi.entity.Note;
import com.example.portalapi.entity.dto.NoteDTO;
import com.example.portalapi.service.NoteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @GetMapping("/notes")
    Page<NoteDTO> getNotes(@PageableDefault(size = 20) Pageable paging){
        return noteService.getNotesWithUsername(paging);
    }

    @GetMapping("/notes/{id}")
    ResponseEntity<Optional<NoteDTO>> getNoteById(@PathVariable("id") Long id) {
        return ResponseEntity.ok( this.noteService.getNoteById(id));
    }

    @GetMapping("/notes/users/{id}")
    Page<NoteDTO> getNoteByUserId(@PathVariable("id") Long id, @PageableDefault(size = 20) Pageable paging) {
        return this.noteService.getNoteByUserId(id, paging);
    }

    @PostMapping("/notes")
    ResponseEntity<NoteDTO> createNote(@RequestBody NoteDTO noteDTO){
        NoteDTO note = this.noteService.save(noteDTO);
        if (note == null) {
            return ResponseEntity.badRequest().build();
        } else {

            return new ResponseEntity<NoteDTO>(note, HttpStatus.CREATED);
        }
    }

    @PutMapping("/notes/{id}")
    ResponseEntity<Note> updateNote(@RequestBody NoteDTO noteDTO){
        Note note = this.noteService.update(noteDTO);
        if (note == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<Note>(note, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/notes/{id}")
    ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
