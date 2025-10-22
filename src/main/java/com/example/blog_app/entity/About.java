package com.example.blog_app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorName;
    private String position;

    @Column(length = 2000)
    private String description;

    private String avatarUrl;
    private String github;
    private String linkedin;
}
