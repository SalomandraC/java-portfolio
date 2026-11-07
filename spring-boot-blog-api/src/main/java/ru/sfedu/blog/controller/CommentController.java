package ru.sfedu.blog.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.blog.dto.CommentRequest;
import ru.sfedu.blog.exception.NotFoundException;
import ru.sfedu.blog.model.Comment;
import ru.sfedu.blog.repository.CommentRepository;
import ru.sfedu.blog.repository.PostRepository;

import java.net.URI;
import java.util.List;

// Без @RequestMapping на классе: эндпоинты живут под двумя префиксами — /posts/{id}/comments и /comments/{id}
@RestController
public class CommentController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentController(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/posts/{postId}/comments")
    public List<Comment> getByPost(@PathVariable long postId) {
        requirePost(postId);
        return commentRepository.findByPostId(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> create(@PathVariable long postId,
                                          @Valid @RequestBody CommentRequest request) {
        requirePost(postId);
        Comment comment = new Comment(postId, request.author(), request.text());
        commentRepository.save(comment);
        return ResponseEntity
                .created(URI.create("/comments/" + comment.getId()))
                .body(comment);
    }

    @GetMapping("/comments/{id}")
    public Comment getById(@PathVariable long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий " + id + " не найден"));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий " + id + " не найден"));
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Без этой проверки запрос к несуществующему посту вернул бы 200 и пустой список вместо 404
    private void requirePost(long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Пост " + postId + " не найден"));
    }
}
