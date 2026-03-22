package com.library.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

        bookResponse = BookResponse.builder().bookId(1L).isbn("12345").title("Java Programming").author("John Doe").build();
    }

    @Test
    void registerBook_shouldReturnCreatedBook() throws Exception {
        when(bookService.registerBook(any(BookRequest.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/api/libraries/v1/books").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookRequest))).andExpect(status().isCreated()).andExpect(jsonPath("$.bookId").value(1L)).andExpect(jsonPath("$.isbn").value("12345")).andExpect(jsonPath("$.title").value("Java Programming")).andExpect(jsonPath("$.author").value("John Doe"));
    }

    @Test
    void listBooks_shouldReturnAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(bookResponse));

        mockMvc.perform(get("/api/libraries/v1/books").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$[0].bookId").value(1L)).andExpect(jsonPath("$[0].isbn").value("12345")).andExpect(jsonPath("$[0].title").value("Java Programming")).andExpect(jsonPath("$[0].author").value("John Doe"));
    }
}