package com.example.portalapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Registration {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String email;
    private final String password;
}
