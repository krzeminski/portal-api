package com.example.portalapi.controller;

import com.example.portalapi.entity.Note;
import com.example.portalapi.entity.dto.NoteDTO;
import com.example.portalapi.enumeration.Role;
import com.example.portalapi.exception.NotesNotFoundException;
import com.example.portalapi.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.portalapi.constant.NotesConstant.NOTES_NOT_FOUND_FOR_USER;
import static com.example.portalapi.constant.NotesConstant.NOTE_NOT_FOUND_FOR_ID;

@Slf4j
@RestController
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/api/notes")
    Page<NoteDTO> getNotes(@PageableDefault(size = 20) Pageable paging, Authentication authentication) throws NotesNotFoundException {
        String email = (String) authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (role.isEmpty()) {
            throw new NotesNotFoundException(NOTES_NOT_FOUND_FOR_USER);
        }
        if (role.equals(Role.USER.name())) {
            log.info(role);
            return noteService.getNoteByUser(email, paging);
        } else {
            return noteService.getNotes(paging);
        }
    }

    @GetMapping("/api/notes/{id}")
//    @PostAuthorize("returnObject.authorEmail == authentication.name")
    @PreAuthorize("hasAuthority('ADMIN') || (@noteService.findById(#id).orElse(new com.portal.api.notes.Note()).user == @userRepository.findByEmail(authentication.principal).get())")
    NoteDTO getNoteById(@PathVariable("id") Long id) throws NotesNotFoundException {
        //        return ResponseEntity.ok(noteDTO);
        return this.noteService.getNoteById(id).orElseThrow(() -> new NotesNotFoundException(NOTE_NOT_FOUND_FOR_ID));
    }

    @GetMapping("/api/notes/users/{id}")
    Page<NoteDTO> getNoteByUserId(@PathVariable("id") Long id, @PageableDefault(size = 20) Pageable paging) {
        return this.noteService.getNoteByUserId(id, paging);
    }

    @PostMapping("/api/notes")
    ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) {
        NoteDTO note = this.noteService.save(noteDTO);
        if (note == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<NoteDTO>(note, HttpStatus.CREATED);
        }
    }

    @PutMapping("/api/notes/{id}")
    ResponseEntity<Note> updateNote(@Valid @RequestBody NoteDTO noteDTO) {
        Note note = this.noteService.update(noteDTO);
        if (note == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<Note>(note, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/api/notes/{id}")
    ResponseEntity<?> deleteNote(@PathVariable Long id) {
        noteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
