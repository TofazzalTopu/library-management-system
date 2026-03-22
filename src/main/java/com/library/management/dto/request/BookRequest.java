package com.library.management.dto.request;

import com.library.management.model.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRequest {

    @NotBlank(message = "ISBN must not be blank")
    @Size(min = 2, max = 100, message = "ISBN must be between 2 and 100 characters")
    private String isbn;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(min = 2, max = 100, message = "Author must be between 2 and 100 characters")
    private String author;

    public Book toEntity() {
        Book book = new Book();
        book.setIsbn(this.isbn);
        book.setTitle(this.title);
        book.setAuthor(this.author);
        return book;
    }

}