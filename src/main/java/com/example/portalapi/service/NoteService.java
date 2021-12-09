package com.example.portalapi.service;

import com.example.portalapi.entity.Note;
import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.NoteDTO;
import com.example.portalapi.entity.dto.mapper.NoteEntityToDTOMapper;
import com.example.portalapi.repository.NoteRepository;
import com.example.portalapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.portalapi.constant.UserConstant.NO_USER_FOUND_BY_EMAIL;

@Service
public class NoteService {
    private final NoteEntityToDTOMapper noteEntityToDTOMapper;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, NoteEntityToDTOMapper noteEntityToDTOMapper, UserRepository userRepository) {
        this.noteEntityToDTOMapper = noteEntityToDTOMapper;
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Page<NoteDTO> getNotes(Pageable paging) {
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
    public Optional<Note> findById(Long id) {
        return this.noteRepository.findById(id);
    }

    public boolean existById(Long id){
        return this.noteRepository.existsById(id);
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
    @Transactional
    public Page<NoteDTO> getNoteByUser(String email, Pageable paging) {
        return new PageImpl<>(
                noteRepository.findByUser_Email(email, paging)
                        .stream()
                        .map(NoteEntityToDTOMapper::convertToNoteDTO)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public NoteDTO save(NoteDTO noteDTO) {
        Note note = new Note();
        note.setUser(userRepository.findByEmail(noteDTO.getAuthorEmail()).orElse(null));
        note.setTitle(noteDTO.getTitle());
        note.setText(noteDTO.getText());
        note.setValue(noteDTO.getValue());
        Note saved = noteRepository.save(note);
        return NoteEntityToDTOMapper.convertToNoteDTO(saved);
    }

    public Note update(NoteDTO noteDTO) {
        Optional<Note> opt = noteRepository.findById(noteDTO.getId());
        if (opt.isPresent()) {
            Note note = opt.get();
            note.setTitle(noteDTO.getTitle());
            note.setText(noteDTO.getText());
            note.setValue(noteDTO.getValue());
            return noteRepository.save(note);
        } else {
            return null;
        }
    }
    public Note convert(NoteDTO noteDTO, String email) {
        Optional<Note> opt = noteRepository.findById(noteDTO.getId());
        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(NO_USER_FOUND_BY_EMAIL));
        if (opt.isPresent()) {
            Note note = opt.get();
            note.setTitle(noteDTO.getTitle());
            note.setText(noteDTO.getText());
            note.setValue(noteDTO.getValue());
            note.setUser(user);
            return noteRepository.save(note);
        } else {
            return null;
        }
    }

    public void deleteById(Long id) {
        noteRepository.deleteById(id);
    }

}
