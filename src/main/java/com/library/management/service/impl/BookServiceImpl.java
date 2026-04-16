package com.library.management.service.impl;

import com.library.management.constants.Constants;
import com.library.management.dto.request.BookRequest;
import com.library.management.dto.response.BookResponse;
import com.library.management.exceptions.BookRegistrationFailureException;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.model.Book;
import com.library.management.repository.BookRepository;
import com.library.management.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Caching(
            put = {
                    @CachePut(value = Constants.CACHE_NAME_BOOK, key = "#result.id")
            },
            evict = {
                    @CacheEvict(value = Constants.CACHE_NAME_BOOK, key = "'ALL'")
            }
    )
    @Transactional
    public BookResponse registerBook(BookRequest bookRequest) {
        log.info("Register book with ISBN={}", bookRequest.getIsbn());

        Optional<Book> existingBookOpt = bookRepository.findFirstByIsbnOrderByIdAsc(bookRequest.getIsbn());

        existingBookOpt.ifPresent(book -> validateBookISBNConsistency(book, bookRequest));
        Book savedBook = bookRepository.save(bookRequest.toEntity());

        return BookResponse.of(savedBook);
    }

    @Override
    @Cacheable(
            value = Constants.CACHE_NAME_BORROWER,
            key = "'PAGE_0'",
            condition = "#pageable.pageNumber == 0"
    )
    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {

        log.info("Fetching books page={} size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> bookPage = bookRepository.findAll(pageable);

        return bookPage.map(BookResponse::of);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Book {} not found", id);
                    return new BorrowBookFailedException(Constants.BOOK_NOT_FOUND);
                });
    }

    @Cacheable(value = Constants.CACHE_NAME_BOOK, key = "#id")
    public BookResponse getBookById(Long id) {
        Book book = findById(id);
        return BookResponse.of(book);
    }

    /*
     * Validate same isbn existence
     */
    public void validateBookISBNConsistency(Book existingBook, BookRequest bookRequest) throws BookRegistrationFailureException {
        log.warn("Book with ISBN={} already exists. Validating consistency...", bookRequest.getIsbn());
        // Validate ISBN. same ISBN => must have same title and author
        if (!existingBook.getTitle().equals(bookRequest.getTitle()) ||
                !existingBook.getAuthor().equals(bookRequest.getAuthor())) {

            String errorMessage = String.format(
                    "Invalid book registration: ISBN %s already exists with title='%s' and author='%s', "
                            + "but attempted registration has title='%s' and author='%s'.",
                    bookRequest.getIsbn(),
                    bookRequest.getTitle(), bookRequest.getAuthor(),
                    existingBook.getTitle(), existingBook.getAuthor()
            );

            log.warn("Book ISBN validation failed: {}", errorMessage);

            throw new BookRegistrationFailureException(errorMessage);
        }
    }

}
