package ru.sfedu.blog.repository;

import org.springframework.stereotype.Repository;
import ru.sfedu.blog.model.Comment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CommentRepository {
    private final Map<Long, Comment> comments = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<Comment> findAll() {
        return comments.values().stream()
                .sorted(Comparator.comparingLong(Comment::getId))
                .toList();
    }

    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(comments.get(id));
    }

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            comment.setId(idGenerator.incrementAndGet());
        }
        comments.put(comment.getId(), comment);
        return comment;
    }

    public void deleteById(long id) {
        comments.remove(id);
    }

    public List<Comment> findByPostId(long postId) {
        return comments.values().stream()
                .filter(comment -> comment.getPostId() == postId)
                .sorted(Comparator.comparingLong(Comment::getId))
                .toList();
    }

    public void deleteByPostId(long postId) {
        comments.values().removeIf(c -> c.getPostId() == postId);
    }
}
