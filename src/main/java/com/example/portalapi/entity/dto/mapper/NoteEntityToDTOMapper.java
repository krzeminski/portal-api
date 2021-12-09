package com.example.portalapi.entity.dto.mapper;

import com.example.portalapi.entity.Note;
import com.example.portalapi.entity.dto.NoteDTO;
import org.springframework.stereotype.Component;

@Component
public class NoteEntityToDTOMapper {
    private NoteEntityToDTOMapper() {
    }

    public static NoteDTO convertToNoteDTO(Note note) {
        return new NoteDTO( note.getId(),
                            note.getTitle(),
                            note.getText(),
                            note.getValue(),
                            note.getCreationDate(),
                            note.getUpdateDate(),
                            note.getUser().getUsernameDTO(),
                            note.getUser().getEmail());
    }
}
