package com.example.portalapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "award")
@Getter
@Setter
public class Award {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "rank")
    private int rank;

    //    @ManyToMany(mappedBy = "awards", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnore
    @ManyToMany(
            mappedBy = "awards",
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<User> recipients = new HashSet<>();


    public void addRecipient(User recipient) {
        recipients.add(recipient);
        recipient.getAwards().add(this);
    }
}
