package com.example.portalapi.entity.dto;

import com.example.portalapi.entity.Award;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties({"award"})
public class UserDTO {
    @JsonProperty("id")
    @NotNull
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
    @JsonProperty("isLocked")
    private boolean isLocked;
    @JsonProperty("isActive")
    private boolean isActive;
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
                   boolean isLocked,
                   boolean isActive,
                   Set<Award> awards) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.joinDate = joinDate;
        this.role = role;
        this.isLocked = isLocked;
        this.isActive = isActive;
        this.awards = awards;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<Award> getAwards() {
        return awards;
    }

    public void setAwards(Set<Award> awards) {
        this.awards = awards;
    }

}
