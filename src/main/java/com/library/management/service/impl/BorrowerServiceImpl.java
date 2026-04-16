package com.library.management.service.impl;

import com.library.management.constants.Constants;
import com.library.management.dto.request.BorrowerRequest;
import com.library.management.dto.response.BorrowerResponse;
import com.library.management.exceptions.BorrowBookFailedException;
import com.library.management.exceptions.BorrowerFailedException;
import com.library.management.model.Borrower;
import com.library.management.repository.BorrowerRepository;
import com.library.management.service.BorrowerService;
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
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;

    @Override
    @Caching(
            put = {
                    @CachePut(value = Constants.CACHE_NAME_BORROWER, key = "#result.id")
            },
            evict = {
                    @CacheEvict(value = Constants.CACHE_NAME_BORROWER, key = "'ALL'")
            }
    )
    @Transactional
    public BorrowerResponse registerBorrower(BorrowerRequest borrowerRequest) {
        log.info("Attempting to register borrower with name={} and email={}",
                borrowerRequest.getName(), borrowerRequest.getEmail());

        Optional<Borrower> existing = borrowerRepository.findByEmail(borrowerRequest.getEmail());

        if (existing.isPresent()) {
            throw new BorrowerFailedException(Constants.BORROWER_EMAIL_ALREADY_EXISTS);
        }
        Borrower savedBorrower = borrowerRepository.save(borrowerRequest.toEntity());

        BorrowerResponse response = BorrowerResponse.of(savedBorrower);
        log.info("Borrower successfully registered with ID={} \n BorrowerResponse: {}", response.getId(), response);
        return response;
    }

    @Override
    @Cacheable(
            value = Constants.CACHE_NAME_BORROWER,
            key = "'PAGE_0'",
            condition = "#pageable.pageNumber == 0"
    )
    @Transactional(readOnly = true)
    public Page<BorrowerResponse> getAllBorrowers(Pageable pageable) {

        log.info("Fetching borrowers page={} size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Borrower> borrowerPage = borrowerRepository.findAll(pageable);

        log.debug("Fetched {} borrowers", borrowerPage.getNumberOfElements());

        return borrowerPage.map(BorrowerResponse::of);
    }

    @Override
    @Transactional(readOnly = true)
    public Borrower findById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Borrower {} not found", id);
                    return new BorrowBookFailedException(Constants.BORROWER_NOT_FOUND);
                });
    }

    @Cacheable(value = Constants.CACHE_NAME_BOOK, key = "#id")
    public BorrowerResponse getBorrowerById(Long id) {
        Borrower book = findById(id);
        return BorrowerResponse.of(book);
    }
}
