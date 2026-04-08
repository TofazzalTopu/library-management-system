package com.library.management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management.constants.Constants;
import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.service.BorrowerService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BorrowerService borrowerService;

    private BorrowerRequest borrowerRequest;
    private BorrowerResponse borrowerResponse;

    @BeforeEach
    void setUp() {
        borrowerRequest = BorrowerRequest.builder()
                .name("Alice Johnson")
                .email("alice@example.com")
                .build();

        borrowerResponse = BorrowerResponse.builder()
                .borrowerId(1L)
                .name("Alice Johnson")
                .email("alice@example.com")
                .build();
    }

    @Test
    void registerBorrower_shouldReturnCreatedBorrower() throws Exception {
        when(borrowerService.registerBorrower(any(BorrowerRequest.class)))
                .thenReturn(borrowerResponse);

        mockMvc.perform(post(Constants.BORROW_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(borrowerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.borrowerId").value(1))
                .andExpect(jsonPath("$.name").value("Alice Johnson"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void bookList_shouldReturnPagedBorrowers() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        Page<BorrowerResponse> page = new PageImpl<>(
                List.of(borrowerResponse),
                pageable,
                1
        );

        when(borrowerService.getAllBorrowers(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get(Constants.BORROW_API_ENDPOINT)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())

                // content
                .andExpect(jsonPath("$.content[0].borrowerId").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Alice Johnson"))
                .andExpect(jsonPath("$.content[0].email").value("alice@example.com"))

                // pagination metadata
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0));
    }
}