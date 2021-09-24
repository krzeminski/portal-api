package com.example.portalapi.repository;

import com.example.portalapi.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200/")
public interface NoteRepository extends JpaRepository<Note, Long> {
}
