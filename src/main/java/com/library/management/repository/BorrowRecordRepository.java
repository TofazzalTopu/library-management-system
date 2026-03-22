package com.library.management.repository;

import com.library.management.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

  Optional<BorrowRecord> findById(Long id);
  boolean existsByBookIdAndActiveTrue(Long bookId);
  List<BorrowRecord> findByActiveTrue();
}