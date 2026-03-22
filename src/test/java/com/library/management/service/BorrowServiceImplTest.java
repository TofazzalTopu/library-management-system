package com.library.management.service;

import com.library.management.dto.response.BorrowRecordResponse;
import com.library.management.dto.response.BorrowResponse;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.exceptions.BorrowStateException;
import com.library.management.exceptions.ReturnBookFailedException;
import com.library.management.model.Book;
import com.library.management.model.BorrowRecord;
import com.library.management.model.Borrower;
import com.library.management.repository.BorrowRecordRepository;
import com.library.management.service.impl.BorrowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @Mock
    private BookService bookService;

    @Mock
    private BorrowerService borrowerService;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    private Book testBook;
    private Borrower testBorrower;
    private BorrowRecord testBorrowRecord;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setIsbn("12345");
        testBook.setTitle("Java Programming");
        testBook.setAuthor("John Doe");

        testBorrower = new Borrower();
        testBorrower.setId(1L);
        testBorrower.setName("Alice Johnson");
        testBorrower.setEmail("alice@example.com");

        testBorrowRecord = new BorrowRecord();
        testBorrowRecord.setRecordId(1L);
        testBorrowRecord.setBook(testBook);
        testBorrowRecord.setBorrower(testBorrower);
        testBorrowRecord.setBorrowMsg("Please handle with care");
        testBorrowRecord.setActive(true);
        testBorrowRecord.setBorrowedAt(LocalDateTime.now());
    }

    @Test
    void borrowBook_shouldSaveBorrowRecordSuccessfully() {
        when(borrowRecordRepository.existsByBookIdAndActiveTrue(testBook.getId())).thenReturn(false);
        when(bookService.findById(testBook.getId())).thenReturn(testBook);
        when(borrowerService.findById(testBorrower.getId())).thenReturn(testBorrower);
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // return the actual saved record

        BorrowResponse response = borrowService.borrowBook(testBorrower.getId(), testBook.getId(), "Handle with care");

        var captor = org.mockito.ArgumentCaptor.forClass(BorrowRecord.class);
        verify(borrowRecordRepository, times(1)).save(captor.capture());
        BorrowRecord savedRecord = captor.getValue();

        // Verify entity fields
        assertThat(savedRecord.getBook()).isEqualTo(testBook);
        assertThat(savedRecord.getBorrower()).isEqualTo(testBorrower);
        assertThat(savedRecord.getBorrowMsg()).isEqualTo("Handle with care");
        assertThat(savedRecord.isActive()).isTrue();
        assertThat(savedRecord.getBorrowedAt()).isNotNull();

        // Verify DTO is correctly created
        assertThat(response).isNotNull();
        assertThat(response.getRecordId()).isEqualTo(savedRecord.getRecordId());
        assertThat(response.getBorrowerId()).isEqualTo(savedRecord.getBorrower().getId());
        assertThat(response.getBookId()).isEqualTo(savedRecord.getBook().getId());
        assertThat(response.getMessage()).isEqualTo(savedRecord.getBorrowMsg());
    }

    @Test
    void borrowBook_shouldThrowException_whenBookAlreadyBorrowed() {
        when(borrowRecordRepository.existsByBookIdAndActiveTrue(testBook.getId())).thenReturn(true);

        Long borrowerId = testBorrower.getId();
        Long bookId = testBook.getId();

        assertThrows(BorrowBookFailedException.class,
                () -> borrowService.borrowBook(borrowerId, bookId, "msg"));

        verify(borrowRecordRepository, never()).save(any(BorrowRecord.class));
    }

    @Test
    void returnBook_shouldMarkBorrowRecordInactive() {
        testBorrowRecord.setActive(true);
        when(borrowRecordRepository.findById(testBorrowRecord.getRecordId())).thenReturn(Optional.of(testBorrowRecord));
        when(borrowRecordRepository.save(any(BorrowRecord.class))).thenAnswer(i -> i.getArguments()[0]);

        BorrowResponse response = borrowService.returnBook(testBorrowRecord.getRecordId());

        assertThat(response).isNotNull();
        assertThat(testBorrowRecord.isActive()).isFalse();
        assertThat(testBorrowRecord.getBorrowMsg()).contains("Returned");

        verify(borrowRecordRepository, times(1)).save(any(BorrowRecord.class));
    }

    @Test
    void returnBook_shouldThrowException_whenRecordNotFound() {
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReturnBookFailedException.class, () -> borrowService.returnBook(1L));
    }

    @Test
    void returnBook_shouldThrowException_whenAlreadyReturned() {
        testBorrowRecord.setActive(false);
        when(borrowRecordRepository.findById(testBorrowRecord.getRecordId())).thenReturn(Optional.of(testBorrowRecord));

        Long recordId = testBorrowRecord.getRecordId();

        assertThrows(BorrowStateException.class,
                () -> borrowService.returnBook(recordId));}

    @Test
    void getCurrentBorrowRecords_shouldReturnActiveBorrowRecords() {
        when(borrowRecordRepository.findByActiveTrue()).thenReturn(List.of(testBorrowRecord));

        List<BorrowRecordResponse> responses = borrowService.getCurrentBorrowRecords();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getBorrowerName()).isEqualTo("Alice Johnson");
        assertThat(responses.get(0).getBookTitle()).isEqualTo("Java Programming");

        verify(borrowRecordRepository, times(1)).findByActiveTrue();
    }
}
