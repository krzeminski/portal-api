package com.example.portalapi.entity.dto;

import com.example.portalapi.utility.ValidEmail;
import com.example.portalapi.utility.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    @ValidEmail
    private String email;
    @ValidPassword
    private String password;


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
