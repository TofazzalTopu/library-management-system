CREATE TABLE book (
    book_id BIGINT PRIMARY KEY,
    isbn VARCHAR(100) NOT NULL,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL
);

CREATE TABLE borrower (
    borrower_id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE borrow_record (
    record_id BIGINT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    borrower_id BIGINT NOT NULL,
    borrowed_at DATE NOT NULL,
    returned_at DATE,
    active BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (borrower_id) REFERENCES borrower(borrower_id)
);


