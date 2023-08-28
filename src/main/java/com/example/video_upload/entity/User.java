package com.example.video_upload.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cannot be empty")
    @Column(unique = true)
    private String username;

    @OneToMany(mappedBy = "user")
    private List<Video> videos = new ArrayList<>();


    @OneToOne(optional = true)
    @JoinColumn(name = "token_id",referencedColumnName = "id")
    private Token token ;

}