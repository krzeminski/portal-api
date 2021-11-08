package com.example.portalapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter
@Setter
public class User {

    public enum Role {
        USER, MODERATOR, ADMIN;

        private static final Map<String, Role> MAP = Stream.of(Role.values()).collect(Collectors.toMap(Enum::name, Function.identity()));

        public static Role fromName(String name) {
            return MAP.get(name);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "user_name")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "join_date", updatable=false)
    @CreationTimestamp
    private Date joinDate;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "locked")
    private boolean isLocked;

    //    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @ManyToMany
    @JoinTable(
            name = "distributed_awards",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "award_id"))
    private Set<Award> awards;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @ToString.Exclude
    private Collection<Note> notes;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Set<Award> getAwards() {
        return awards;
    }

    public void addAward(Award award) {
        this.awards.add(award);
    }

    public void setAwards(Set<Award> awards) {
        this.awards = awards;
    }

    public Collection<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public void setNotes(Collection<Note> notes) {
        this.notes = notes;
    }
}
