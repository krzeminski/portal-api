package com.example.portalapi.service;

import com.example.portalapi.entity.ConfirmationToken;
import com.example.portalapi.enumeration.Role;
import com.example.portalapi.entity.User;
import com.example.portalapi.entity.dto.Registration;
import com.example.portalapi.utility.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final EmailService emailService;

    public String register(Registration request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid " + request.getEmail());
        }

        // TODO: 30.11.2021 add more params
        String token = userService.register(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getUsername(),
                        request.getEmail(),
                        request.getPassword(),
                        Role.USER
                )
        );

        String link = "http://localhost:8081/registration/confirm?token=" + token;
        emailService.send(
                request.getEmail(),
                emailService.buildRegistrationConfirmationEmail(request.getFirstName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.activateUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

}
