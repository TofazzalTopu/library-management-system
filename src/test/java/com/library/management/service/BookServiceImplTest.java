package com.library.management.service;

import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.exceptions.BookRegistrationFailureException;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.exceptions.ReturnBookFailedException;
import com.library.management.model.Book;
import com.library.management.repository.BookRepository;
import com.library.management.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    void registerBook_shouldSaveNewBook_whenISBNNotExists() {
        BookRequest request = BookRequest.builder()
                .isbn("12345")
                .title("Java Programming")
                .author("John Doe")
                .build();

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setIsbn(request.getIsbn());
        savedBook.setTitle(request.getTitle());
        savedBook.setAuthor(request.getAuthor());

        // Stub repository
        when(bookRepository.findFirstByIsbnOrderByIdAsc("12345")).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponse response = bookService.registerBook(request);

        assertThat(response).isNotNull();
        assertThat(response.getBookId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Java Programming");

        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void registerBook_shouldThrowException_whenISBNExistsWithDifferentTitleOrAuthor() {
        BookRequest request = BookRequest.builder()
                .isbn("12345")
                .title("Java Programming")
                .author("John Doe")
                .build();

        Book existingBook = new Book();
        existingBook.setId(2L);
        existingBook.setIsbn("12345");
        existingBook.setTitle("Python Programming");
        existingBook.setAuthor("Jane Doe");

        when(bookRepository.findFirstByIsbnOrderByIdAsc("12345")).thenReturn(Optional.of(existingBook));

        assertThrows(BookRegistrationFailureException.class,
                () -> bookService.registerBook(request));

        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void getAllBooks_shouldReturnListOfBookResponses() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setIsbn("111");
        book1.setTitle("Java");
        book1.setAuthor("Author1");

        Book book2 = new Book();
        book2.setId(2L);
        book2.setIsbn("222");
        book2.setTitle("Spring Boot");
        book2.setAuthor("Author2");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        List<BookResponse> books = bookService.getAllBooks();

        assertThat(books).hasSize(2);
        assertThat(books.get(0).getTitle()).isEqualTo("Java");
        assertThat(books.get(1).getTitle()).isEqualTo("Spring Boot");

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnBook_whenBookExists() {
        Book book = new Book();
        book.setId(1L);
        book.setIsbn("123");
        book.setTitle("Java");
        book.setAuthor("John");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.findById(1L);

        assertThat(foundBook).isNotNull();
        assertThat(foundBook.getTitle()).isEqualTo("Java");

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldThrowException_whenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BorrowBookFailedException.class, () -> bookService.findById(1L));

        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void validateBookISBNConsistency_shouldThrowException_whenTitleOrAuthorMismatch() {
        BookRequest request = BookRequest.builder()
                .isbn("12345")
                .title("Java Programming")
                .author("John Doe")
                .build();

        Book existingBook = new Book();
        existingBook.setIsbn("12345");
        existingBook.setTitle("Python Programming");
        existingBook.setAuthor("Jane Doe");

        assertThrows(BookRegistrationFailureException.class,
                () -> bookService.validateBookISBNConsistency(existingBook, request));
    }

    @Test
    void validateBookISBNConsistency_shouldPass_whenTitleAndAuthorMatch() {
        BookRequest request = BookRequest.builder()
                .isbn("12345")
                .title("Java Programming")
                .author("John Doe")
                .build();

        Book existingBook = new Book();
        existingBook.setIsbn("12345");
        existingBook.setTitle("Java Programming");
        existingBook.setAuthor("John Doe");

        // Should not throw exception
        bookService.validateBookISBNConsistency(existingBook, request);
    }

    @Test
    void testBookRegistrationExceptionWithCause() {
        Exception cause = new Exception("Root cause");
        BookRegistrationFailureException ex =
                new BookRegistrationFailureException("Test message", cause);

        assertThat(ex.getMessage()).isEqualTo("Test message");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    void testReturnBookExceptionWithCause() {
        Exception cause = new Exception("Return Book cause");
        ReturnBookFailedException ex =
                new ReturnBookFailedException("Test message", cause);

        assertThat(ex.getMessage()).isEqualTo("Test message");
        assertThat(ex.getCause()).isEqualTo(cause);
    }
    @Test
    void testBorrowBookExceptionWithCause() {
        Exception cause = new Exception("Borrow root cause");
        BorrowBookFailedException ex =
                new BorrowBookFailedException("Test message", cause);

        assertThat(ex.getMessage()).isEqualTo("Test message");
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}