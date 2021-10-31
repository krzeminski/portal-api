package com.example.portalapi.entity.dto;

import com.example.portalapi.entity.Award;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties({"award"})
public class UserDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("profileImageUrl")
    private String profileImageUrl;
    @JsonProperty("joinDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date joinDate;
    @JsonProperty("role")
    private String role;
    @JsonProperty("awards")
    private Set<Award> awards;

    @JsonCreator
    public UserDTO(Long id,
                   String firstName,
                   String lastName,
                   String username,
                   String email,
                   String profileImageUrl,
                   Date joinDate,
                   String role,
                   Set<Award> awards) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.joinDate = joinDate;
        this.role = role;
        this.awards = awards;
    }
}
