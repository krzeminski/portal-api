package com.example.portalapi.controller;

import com.example.portalapi.entity.dto.Registration;
import com.example.portalapi.exception.EmailExistsException;
import com.example.portalapi.exception.UserNotFoundException;
import com.example.portalapi.exception.UsernameExistsException;
import com.example.portalapi.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@Validated
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping(path = "/api/registration")
    public String register(@Valid @RequestBody Registration registration) throws EmailExistsException, UsernameExistsException {
        return registrationService.register(registration);
    }

    @GetMapping(path = "/api/registration/confirm")
    public String confirm(@RequestParam("token") String token) throws UserNotFoundException {
        return registrationService.confirmToken(token);
    }

}
