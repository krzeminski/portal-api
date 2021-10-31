package com.example.portalapi.service;

import com.example.portalapi.entity.dto.NoteDTO;
import com.example.portalapi.entity.dto.mapper.NoteEntityToDTOMapper;
import com.example.portalapi.repository.NoteRepository;
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

    public NoteService(NoteRepository noteRepository, NoteEntityToDTOMapper noteEntityToDTOMapper) {
        this.noteEntityToDTOMapper = noteEntityToDTOMapper;
        this.noteRepository = noteRepository;
    }

    @Transactional
    public Page<NoteDTO> getNotesWithUsername(Pageable paging) {

//        final var noteEntities =
//                noteRepository.findAll(paging);


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
}
