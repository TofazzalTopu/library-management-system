package com.library.management.service;

import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.model.Borrower;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.impl.BorrowerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceImplTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerServiceImpl borrowerService; // Use concrete class

    private BorrowerRequest borrowerRequest;

    @BeforeEach
    void setUp() {
        borrowerRequest = BorrowerRequest.builder()
                .name("Alice Johnson")
                .email("alice@example.com")
                .build();
    }

    @Test
    void registerBorrower_shouldSaveBorrowerSuccessfully() {


        Borrower savedBorrower = new Borrower();
        savedBorrower.setId(1L);
        savedBorrower.setName(borrowerRequest.getName());
        savedBorrower.setEmail(borrowerRequest.getEmail());

        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedBorrower);

        BorrowerResponse response = borrowerService.registerBorrower(borrowerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getBorrowerId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Alice Johnson");

        verify(borrowerRepository, times(1)).save(any(Borrower.class));
    }

    @Test
    void getAllBorrowers_shouldReturnPagedBorrowerResponses_andVerifyPageable() {

        Borrower borrower1 = new Borrower();
        borrower1.setId(1L);
        borrower1.setName("Alice");
        borrower1.setEmail("alice@example.com");

        Borrower borrower2 = new Borrower();
        borrower2.setId(2L);
        borrower2.setName("Bob");
        borrower2.setEmail("bob@example.com");

        Pageable pageable = PageRequest.of(0, 10);

        Page<Borrower> borrowerPage =
                new PageImpl<>(List.of(borrower1, borrower2), pageable, 2);

        when(borrowerRepository.findAll(any(Pageable.class)))
                .thenReturn(borrowerPage);

        Page<BorrowerResponse> result = borrowerService.getAllBorrowers(pageable);

        // Assert - content
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Alice");
        assertThat(result.getContent().get(1).getName()).isEqualTo("Bob");

        // Assert - metadata
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getSize()).isEqualTo(10);

        // Verify Pageable passed correctly
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(borrowerRepository, times(1)).findAll(captor.capture());

        Pageable captured = captor.getValue();
        assertThat(captured.getPageNumber()).isZero();
        assertThat(captured.getPageSize()).isEqualTo(10);
    }

    @Test
    void findById_shouldReturnBorrower_whenBorrowerExists() {
        Borrower borrower = new Borrower();
        borrower.setId(1L);
        borrower.setName("Alice");
        borrower.setEmail("alice@example.com");

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(borrower));

        Borrower found = borrowerService.findById(1L);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Alice");

        verify(borrowerRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenBorrowerNotFound() {
        when(borrowerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BorrowBookFailedException.class, () -> borrowerService.findById(1L));

        verify(borrowerRepository, times(1)).findById(1L);
    }
}