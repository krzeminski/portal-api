package com.example.portalapi.repository;

import com.example.portalapi.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("http://localhost:4200/")
public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByUser_Id(@RequestParam("id") Long id, Pageable pageable);

    
}
