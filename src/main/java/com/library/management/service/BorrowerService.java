package com.library.management.service;

import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.model.Borrower;

import java.util.List;

public interface BorrowerService {

  BorrowerResponse registerBorrower(BorrowerRequest borrowerRequest);

  List<BorrowerResponse> getAllBorrowers();

  Borrower findById(Long id);
}
