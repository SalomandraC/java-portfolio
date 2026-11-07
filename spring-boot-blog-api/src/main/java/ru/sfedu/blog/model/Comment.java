package ru.sfedu.blog.model;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private final Long postId;
    private final String author;
    private final String text;
    private final LocalDateTime createdAt;

    public Comment(Long postId, String author, String text) {
        this.postId = postId;
        this.author = author;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
