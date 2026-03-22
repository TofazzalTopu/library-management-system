CREATE INDEX idx_book_isbn_id ON BOOK(isbn, id);

ALTER TABLE BORROWER
ADD CONSTRAINT uk_borrower_email UNIQUE (email);
