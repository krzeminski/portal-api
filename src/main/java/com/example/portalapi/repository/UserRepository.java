package com.example.portalapi.repository;

import com.example.portalapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(collectionResourceRel = "user", path= "user")
@CrossOrigin("http://localhost:4200/")
public interface UserRepository extends JpaRepository<User, Long> {
}
