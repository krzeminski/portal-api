package com.example.portalapi.controller;

import com.example.portalapi.entity.User;
import com.example.portalapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/myusers")
    Collection<User> users(){
        return this.userRepository.findAll();
    }

    @GetMapping("/myusers/{id}")
    User getUser(){
        return this.userRepository.findByEmail("admin@portal.com");
    }

//    @RequestMapping("/user/{id}")
//    Collection<User> users(){
//        return this.userRepository.findAll();
//
//        User user = repository.findById(id); //
//                .orElseThrow(() -> new EmployeeNotFoundException(id));
//
//        return EntityModel.of(user, //
//                linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
//                linkTo(methodOn(UserController.class).all()).withRel("employees"));
//    }
}
