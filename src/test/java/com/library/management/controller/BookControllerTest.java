package com.library.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.constants.Constants;
import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    private BookRequest bookRequest;
    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        bookRequest = BookRequest.builder().isbn("12345").title("Java Programming").author("John Doe").build();

        bookResponse = BookResponse.builder()
                .id(1L)
                .isbn("12345")
                .title("Java Programming")
                .author("John Doe")
                .build();
    }

    @Test
    void registerBook_shouldReturnCreatedBook() throws Exception {
        when(bookService.registerBook(any(BookRequest.class))).thenReturn(bookResponse);

        mockMvc.perform(post(Constants.BOOKS_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(1L))
                .andExpect(jsonPath("$.isbn").value("12345"))
                .andExpect(jsonPath("$.title").value("Java Programming"))
                .andExpect(jsonPath("$.author").value("John Doe"));
    }

    @Test
    void listBooks_shouldReturnPagedBooks() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        Page<BookResponse> page = new PageImpl<>(
                List.of(bookResponse),
                pageable,
                1
        );

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get(Constants.BOOKS_API_ENDPOINT)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                // content array
                .andExpect(jsonPath("$.content[0].bookId").value(1L))
                .andExpect(jsonPath("$.content[0].isbn").value("12345"))
                .andExpect(jsonPath("$.content[0].title").value("Java Programming"))
                .andExpect(jsonPath("$.content[0].author").value("John Doe"))

                // pagination metadata
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }
}