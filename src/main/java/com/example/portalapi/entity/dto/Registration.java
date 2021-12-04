package com.example.portalapi.entity.dto;

import com.example.portalapi.utility.ValidEmail;
import com.example.portalapi.utility.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Registration {
    @NotNull
    @Size(min = 1, message = "{Size.registration.firstName}")
    private final String firstName;
    @NotNull
    @Size(min = 1, message = "{Size.registration.lastName}")
    private final String lastName;
    @NotNull
    @Size(min = 1, message = "{Size.registration.username}")
    private final String username;
    @NotNull
    @ValidEmail
    @Email
    @Size(min = 1, message = "{Size.registration.email}")
    private final String email;
    @ValidPassword
    @NotNull
    @NotBlank(message = "Password is mandatory")
    private final String password;
}
