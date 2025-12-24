package com.example.personal_blog.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "posts")
public class BlogPost {

    @Id
    private String id;

    @NotBlank(message = "Title required")
    private String title;
    private String content;
    private String author;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Set<String> likedBy = new HashSet<>();
    public int getLikeCount() {
        return likedBy.size();
    }
}
