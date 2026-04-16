package com.library.management.service.impl;

import com.library.management.constants.Constants;
import com.library.management.dto.response.BorrowRecordResponse;
import com.library.management.dto.response.BorrowResponse;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.exceptions.BorrowStateException;
import com.library.management.exceptions.ReturnBookFailedException;
import com.library.management.model.Book;
import com.library.management.model.BorrowRecord;
import com.library.management.model.Borrower;
import com.library.management.repository.BorrowRecordRepository;
import com.library.management.service.BookService;
import com.library.management.service.BorrowService;
import com.library.management.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BookService bookService;
    private final BorrowerService borrowerService;
    private final BorrowRecordRepository borrowRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public BorrowRecord findById(Long id) {
        return borrowRecordRepository.findById(id).orElseThrow(() -> {
            log.error("Return failed: Borrow record {} not found", id);
            return new ReturnBookFailedException("Borrow record not found");
        });
    }

    @Override
    @Transactional
    public BorrowResponse borrowBook(Long borrowerId, Long bookId, String message) {

        log.info("Borrow request received: borrowerId={}, bookId={}, message={}", borrowerId, bookId, message);

        // verify if book already borrowed
        verifyIfTheBookIsAlreadyBorrowed(bookId);

        Book book = bookService.findById(bookId);

        Borrower borrower = borrowerService.findById(borrowerId);

        BorrowRecord borrowRecord = buildBorrowRecord(book, borrower, message);

        BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);

        BorrowResponse response = BorrowResponse.of(savedRecord);

        log.info("Borrow successful: borrowRecordId={}, borrowerId={}, bookId={}", response.getRecordId(), borrowerId, bookId);

        return response;
    }

    // verify if book already borrowed
    private void verifyIfTheBookIsAlreadyBorrowed(Long bookId) {
        if (borrowRecordRepository.existsByBookIdAndActiveTrue(bookId)) {
            log.warn(Constants.BORROW_FAILED_BOOK_IS_ALREADY_BORROWED, bookId);
            throw new BorrowBookFailedException(Constants.BOOK_IS_ALREADY_BORROWED);
        }
    }

    @Transactional
    public BorrowResponse returnBook(Long borrowRecordId) {
        log.info("Return request received: borrowRecordId={}", borrowRecordId);

        BorrowRecord borrowRecord = findById(borrowRecordId);
        if (!borrowRecord.isActive()) {
            throw new BorrowStateException(Constants.BOOK_ALREADY_RETURNED);
        }

        BorrowRecord returnBorrowRecord = createReturn(borrowRecord);
        BorrowRecord savedRecord = borrowRecordRepository.save(returnBorrowRecord);

        return BorrowResponse.of(savedRecord);
    }

    @Transactional(readOnly = true)
    public List<BorrowRecordResponse> getCurrentBorrowRecords() {
        log.info("Fetching all currently active borrow records");

        List<BorrowRecord> activeRecords = borrowRecordRepository.findByActiveTrue();
        log.debug("Fetched {} active borrow records", activeRecords.size());

        return activeRecords.stream().map(BorrowRecordResponse::of).toList();
    }

    public static BorrowRecord buildBorrowRecord(Book book, Borrower borrower, String message) {

        log.info("Creating borrow record: bookId={}, borrowerId={}, message={}", book.getId(), borrower.getId(), message);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setBorrower(borrower);
        borrowRecord.setBorrowMsg(message);
        borrowRecord.setActive(true);
        borrowRecord.setBorrowedAt(LocalDateTime.now());

        log.debug("Borrow record created successfully: {}", borrowRecord);

        return borrowRecord;
    }

    private static BorrowRecord createReturn(BorrowRecord returnRecord) {

        log.info("Returning borrow record: borrowRecordId={}, bookId={}, borrowerId={}", returnRecord.getRecordId(), returnRecord.getBook().getId(), returnRecord.getBorrower().getId());

        returnRecord.setActive(false);
        returnRecord.setReturnedAt(LocalDateTime.now());
        returnRecord.setBorrowMsg(returnRecord.getBorrowMsg() == null ? "Returned" : returnRecord.getBorrowMsg() + " - Returned");
        return returnRecord;
    }
}
