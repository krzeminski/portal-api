package com.example.portalapi.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "note")
@Getter
@Setter
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "value")
    private int value;

    @Column(name = "creation_date")
    @CreationTimestamp
    private Date creationDate;

    @Column(name = "update_date")
    @UpdateTimestamp
    private Date updateDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private User user;
}
