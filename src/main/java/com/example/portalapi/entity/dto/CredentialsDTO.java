/*
 * Copyright 2020 Motorola Solutions, Inc.
 * All Rights Reserved.
 * Motorola Solutions Confidential Restricted
 */
package com.example.portalapi.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CredentialsDTO {

    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    @JsonCreator
    public CredentialsDTO(
            String email,
            String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
