package ru.sfedu.blog.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.blog.dto.PostRequest;
import ru.sfedu.blog.exception.NotFoundException;
import ru.sfedu.blog.model.Post;
import ru.sfedu.blog.repository.CommentRepository;
import ru.sfedu.blog.repository.PostRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    public PostController(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост " + id + " не найден"));
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody PostRequest request) {
        Post post = new Post(request.title(), request.content(), request.author());
        postRepository.save(post);
        return ResponseEntity
                .created(URI.create("/posts/" + post.getId()))
                .body(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable long id, @Valid @RequestBody PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост " + id + " не найден"));
        post.setTitle(request.title());
        post.setContent(request.content());
        post.setAuthor(request.author());
        return post;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост " + id + " не найден"));
        commentRepository.deleteByPostId(id);
        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}