package com.example.video_upload.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class Video {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private long size;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JsonIgnore
    private String hash;

}