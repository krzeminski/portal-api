package com.example.portalapi.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class NoteDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("text")
    private String text;
    @JsonProperty("value")
    private int value;
    @JsonProperty("creationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date creationDate;
    @JsonProperty("updateDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date updateDate;
    @JsonProperty("author")
    private String author;

    @JsonCreator
    public NoteDTO( Long id,
                    String title,
                    String text,
                    int value,
                    Date creationDate,
                    Date updateDate,
                    String author) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.value = value;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.author = author;
    }
}
