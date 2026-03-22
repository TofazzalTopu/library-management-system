package com.library.management.service;

import com.library.management.dto.response.BorrowRecordResponse;
import com.library.management.dto.response.BorrowResponse;
import com.library.management.model.BorrowRecord;

import java.util.List;

public interface BorrowService {

  BorrowRecord findById(Long id);

  BorrowResponse borrowBook(Long borrowerId, Long bookId, String message);

  BorrowResponse returnBook(Long borrowRecordId);

  List<BorrowRecordResponse> getCurrentBorrowRecords();
}
