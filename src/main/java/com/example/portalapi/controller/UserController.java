package com.example.portalapi.controller;

import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.CredentialsDTO;
import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/users")
    Page<UserDTO> getUsers(@PageableDefault(size = 20) Pageable paging){
        return this.userService.getUsersWithAwards(paging);
    }

    @GetMapping("/users/{id}")
    ResponseEntity<Optional<UserDTO>> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok( this.userService.getUser(id));
    }

    @PostMapping(path = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User user) {
        User u = userService.save(user);
        if (u == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<>(u, HttpStatus.CREATED);
        }
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<User> update(@RequestBody UserDTO userDTO,  @PathVariable("id") Long id) {
        User user = userService.update(userDTO);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/users/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> authenticate(@RequestBody CredentialsDTO credentialsDTO) {
        UserDTO userDTO = userService.authenticate(credentialsDTO);
        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        }
    }

}
