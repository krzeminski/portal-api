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
import java.net.URI;

import static com.example.portalapi.constant.NotesConstant.COULD_NOT_CREATE;
import static com.example.portalapi.constant.NotesConstant.NOTES_NOT_FOUND_FOR_USER;
import static com.example.portalapi.constant.NotesConstant.NOTE_NOT_FOUND_FOR_ID;
import static com.example.portalapi.constant.NotesConstant.YOU_ARE_NOT_AN_OWNER;

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
        if (role.equals(Role.USER.name())) {
            log.info(role);
            return noteService.getNoteByUser(email, paging);
        } else if (role.equals(Role.ADMIN.name()) || role.equals(Role.MODERATOR.name())) {
            return noteService.getNotes(paging);
        } else {
            throw new NotesNotFoundException(NOTES_NOT_FOUND_FOR_USER);
        }
    }

    @GetMapping("/api/notes/{id}")
    NoteDTO getNoteById(@PathVariable("id") Long id, Authentication authentication) throws NotesNotFoundException {
        String email = (String) authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        NoteDTO noteDTO = noteService.getNoteById(id).orElseThrow(() -> new NotesNotFoundException(NOTE_NOT_FOUND_FOR_ID));
        if (role.equals(Role.USER.name()) && email.equals(noteDTO.getAuthorEmail())) {
            return noteDTO;
        } else if (role.equals(Role.ADMIN.name()) || role.equals(Role.MODERATOR.name())) {
            return noteService.getNoteById(id).orElseThrow(() -> new NotesNotFoundException(NOTES_NOT_FOUND_FOR_USER));
        } else {
            throw new NotesNotFoundException(NOTES_NOT_FOUND_FOR_USER);
        }
    }

    @PostMapping("/api/notes")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR', 'USER')")
    ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO, Authentication authentication) throws NotesNotFoundException {
        String email = (String) authentication.getPrincipal();
        noteDTO.setAuthorEmail(email);
        NoteDTO note = this.noteService.save(noteDTO);
        if (note == null) {
            throw new NotesNotFoundException(COULD_NOT_CREATE);
        } else {
            URI location = URI.create(String.format("/notes/%d", note.getId()));
            return ResponseEntity.created(location).build();
        }
    }

    @PutMapping("/api/notes/{id}")
    ResponseEntity<Note> updateNote(@Valid @RequestBody NoteDTO noteDTO, Authentication authentication) throws NotesNotFoundException {
        String email = (String) authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        NoteDTO existingNoteDTO = noteService.getNoteById(noteDTO.getId()).orElseThrow(() -> new NotesNotFoundException(NOTE_NOT_FOUND_FOR_ID));

        if (email.equals(existingNoteDTO.getAuthorEmail())) {
            Note note = this.noteService.update(noteDTO);
            URI location = URI.create(String.format("/notes/%d", note.getId()));
            return ResponseEntity.created(location).build();
        } else if (role.equals(Role.ADMIN.name()) || role.equals(Role.MODERATOR.name())) {
            Note note = this.noteService.convert(noteDTO, email);
            URI location = URI.create(String.format("/notes/%d", note.getId()));
            return ResponseEntity.created(location).build();
        } else {
            throw new NotesNotFoundException(YOU_ARE_NOT_AN_OWNER);
        }
    }

    @DeleteMapping("/api/notes/{id}")
    ResponseEntity<?> deleteNote(@PathVariable Long id, Authentication authentication) throws NotesNotFoundException {
        String email = (String) authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        NoteDTO noteDTO = noteService.getNoteById(id).orElseThrow(() -> new NotesNotFoundException(NOTE_NOT_FOUND_FOR_ID));

        if (email.equals(noteDTO.getAuthorEmail()) || role.equals(Role.ADMIN.name()) || role.equals(Role.MODERATOR.name())) {
            noteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new NotesNotFoundException(YOU_ARE_NOT_AN_OWNER);
        }
    }
}
