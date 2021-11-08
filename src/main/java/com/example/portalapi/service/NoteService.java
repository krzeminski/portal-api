package com.example.portalapi.service;

import com.example.portalapi.entity.Note;
import com.example.portalapi.entity.dto.NoteDTO;
import com.example.portalapi.entity.dto.mapper.NoteEntityToDTOMapper;
import com.example.portalapi.repository.NoteRepository;
import com.example.portalapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteEntityToDTOMapper noteEntityToDTOMapper;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, NoteEntityToDTOMapper noteEntityToDTOMapper, UserRepository userRepository) {
        this.noteEntityToDTOMapper = noteEntityToDTOMapper;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Page<NoteDTO> getNotesWithUsername(Pageable paging) {
        return new PageImpl<>(
                noteRepository.findAll()
                        .stream()
                        .map(NoteEntityToDTOMapper::convertToNoteDTO)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public Optional<NoteDTO> getNoteById(Long id) {
        return this.noteRepository.findById(id).map(NoteEntityToDTOMapper::convertToNoteDTO);
    }

    @Transactional
    public Page<NoteDTO> getNoteByUserId(Long id, Pageable paging) {
        return new PageImpl<>(
                noteRepository.findByUser_Id(id, paging)
                        .stream()
                        .map(NoteEntityToDTOMapper::convertToNoteDTO)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public NoteDTO save(NoteDTO noteDTO){
        Note note = new Note();
        note.setUser(userRepository.findByEmail(noteDTO.getAuthorEmail()));
        note.setTitle(noteDTO.getTitle());
        note.setText(noteDTO.getText());
        note.setValue(noteDTO.getValue());
        note.setValue(noteDTO.getValue());
        Note saved = noteRepository.save(note);
        return NoteEntityToDTOMapper.convertToNoteDTO(saved);
    }

    public Note update(NoteDTO noteDTO){
        Note note = new Note();
        note.setUser(userRepository.findByEmail(noteDTO.getAuthorEmail()));
        note.setId(noteDTO.getId());
        note.setTitle(noteDTO.getTitle());
        note.setText(noteDTO.getText());
        note.setValue(noteDTO.getValue());
        return noteRepository.save(note);
    }

    public void deleteById(Long id){
        noteRepository.deleteById(id);
    }

}
