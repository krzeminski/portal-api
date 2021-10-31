package com.example.portalapi.controller;

import com.example.portalapi.entity.dto.UserDTO;
import com.example.portalapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

//    @GetMapping("/myusers/{id}")
//    User getUser(){
//        return this.userService.findByEmail("admin@portal.com");
//    }

}
