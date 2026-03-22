package com.library.management.service.impl;

import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.model.Borrower;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.BorrowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Override
    public BorrowerResponse registerBorrower(BorrowerRequest borrowerRequest) {
        log.info("Attempting to register borrower with name={} and email={}",
                borrowerRequest.getName(), borrowerRequest.getEmail());

        Borrower savedBorrower = borrowerRepository.save(borrowerRequest.toEntity());
        log.info("Borrower successfully registered with ID={} \n BorrowerResponse: {}", savedBorrower.getId(), savedBorrower);

        return BorrowerResponse.of(savedBorrower);
    }

    @Override
    public List<BorrowerResponse> getAllBorrowers() {
        log.info("Fetching all borrowers from repository");

        List<Borrower> borrowers = borrowerRepository.findAll();
        log.debug("Fetched {} borrowers", borrowers.size());

        return borrowers.stream().map(BorrowerResponse::of).toList();
    }

    @Override
    public Borrower findById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Borrower {} not found", id);
                    return new BorrowBookFailedException("Borrower not found");
                });
    }
}
