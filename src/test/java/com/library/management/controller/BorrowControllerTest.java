package com.library.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.dto.request.BorrowRequest;
import com.library.management.dto.response.BorrowRecordResponse;
import com.library.management.dto.response.BorrowResponse;
import com.library.management.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowController.class)
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BorrowService borrowService;

    private BorrowRequest borrowRequest;
    private BorrowResponse borrowResponse;
    private BorrowRecordResponse borrowRecordResponse;

    @BeforeEach
    void setUp() {
        borrowRequest = BorrowRequest.builder()
                .borrowerId(1L)
                .bookId(10L)
                .borrowMsg("Handle with care")
                .build();

        // BorrowResponse (matches your DTO)
        borrowResponse = BorrowResponse.builder()
                .recordId(100L)
                .borrowerId(1L)
                .bookId(10L)
                .message("Handle with care")
                .hasBorrowed(true)
                .build();

        // BorrowRecordResponse (correct fields only)
        borrowRecordResponse = BorrowRecordResponse.builder()
                .id(100L)
                .bookId(10L)
                .bookTitle("Java Programming")
                .isbn("12345")
                .borrowerId(1L)
                .borrowerName("Alice Johnson")
                .borrowedAt(LocalDateTime.now())
                .build();
    }

    // =========================
    // BORROW BOOK
    // =========================
    @Test
    void borrowBook_shouldReturnBorrowResponse() throws Exception {

        when(borrowService.borrowBook(1L, 10L, "Handle with care"))
                .thenReturn(borrowResponse);

        mockMvc.perform(post("/api/libraries/v1/borrows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recordId").value(100))
                .andExpect(jsonPath("$.borrowerId").value(1))
                .andExpect(jsonPath("$.bookId").value(10))
                .andExpect(jsonPath("$.message").value("Handle with care"))
                .andExpect(jsonPath("$.hasBorrowed").value(true));
    }

    // =========================
    // RETURN BOOK
    // =========================
    @Test
    void returnBook_shouldReturnBorrowResponse() throws Exception {

        when(borrowService.returnBook(100L)).thenReturn(borrowResponse);

        mockMvc.perform(post("/api/libraries/v1/borrows/100/return"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recordId").value(100))
                .andExpect(jsonPath("$.borrowerId").value(1))
                .andExpect(jsonPath("$.bookId").value(10))
                .andExpect(jsonPath("$.message").value("Handle with care"))
                .andExpect(jsonPath("$.hasBorrowed").value(true));
    }

    // =========================
    // GET CURRENT BORROWS
    // =========================
    @Test
    void getCurrentBorrowRecords_shouldReturnList() throws Exception {

        when(borrowService.getCurrentBorrowRecords())
                .thenReturn(List.of(borrowRecordResponse));

        mockMvc.perform(get("/api/libraries/v1/borrows/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))

                .andExpect(jsonPath("$[0].borrowId").value(100))
                .andExpect(jsonPath("$[0].bookId").value(10))
                .andExpect(jsonPath("$[0].bookTitle").value("Java Programming"))
                .andExpect(jsonPath("$[0].isbn").value("12345"))
                .andExpect(jsonPath("$[0].borrowerId").value(1))
                .andExpect(jsonPath("$[0].borrowerName").value("Alice Johnson"))
                .andExpect(jsonPath("$[0].borrowedAt").exists());
    }
}