package com.library.management.service;

import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.model.Borrower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BorrowerService {

  BorrowerResponse registerBorrower(BorrowerRequest borrowerRequest);

  Page<BorrowerResponse> getAllBorrowers(Pageable pageable);

  Borrower findById(Long id);
}
