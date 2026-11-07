package ru.sfedu.blog.repository;

import org.springframework.stereotype.Repository;
import ru.sfedu.blog.model.Post;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<Post> findAll() {
        return posts.values().stream()
                .sorted(Comparator.comparingLong(Post::getId))
                .toList();
    }

    public Optional<Post> findById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(idGenerator.incrementAndGet());
        }
        posts.put(post.getId(), post);
        return post;
    }

    public void deleteById(long id) {
        posts.remove(id);
    }
}
