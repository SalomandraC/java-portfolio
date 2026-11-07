package ru.sfedu.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Автор комментария не может быть пустым")
        @Size(max = 100, message = "Автор — не длиннее 100 символов")
        String author,
        @NotBlank(message = "Текст комментария не может быть пустым")
        @Size(max = 2000, message = "Текст комментария — не длиннее 2000 символов")
        String text
) {
}