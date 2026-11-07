package ru.sfedu.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotBlank(message = "Заголовок не может быть пустым")
        @Size(max = 200, message = "Заголовок — не длиннее 200 символов")
        String title,
        @NotBlank(message = "Контент не может быть пустым")
        @Size(max = 10000, message = "Контент — не длиннее 10000 символов")
        String content,
        @NotBlank(message = "Автор не может быть пустым")
        @Size(max = 100, message = "Автор — не длиннее 100 символов")
        String author
) {
}
