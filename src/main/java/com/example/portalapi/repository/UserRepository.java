package com.example.portalapi.repository;

import com.example.portalapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestResource(collectionResourceRel = "user", path= "user")
@CrossOrigin("http://localhost:4200/")
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByEmail(@RequestParam("email") String email, Pageable pageable);
    Page<User> findByUsername(@RequestParam("username") String username, Pageable pageable);
}
