-- Borrowers
INSERT INTO borrower (borrower_id, name, email)
VALUES (1, 'Jhone Doe', 'jhon@example.com');

INSERT INTO borrower (borrower_id, name, email)
VALUES (2, 'Steve', 'steve@example.com');

-- Books
INSERT INTO book (book_id, isbn, title, author)
VALUES (1, '234126789324596', 'Spring Boot: Up and Running', 'Mark Heckler');

INSERT INTO book (book_id, isbn, title, author)
VALUES (2, '892352683451283', 'Pro Spring', 'Rob Harrop');

-- Another copy of the same book (multiple copies allowed)
INSERT INTO book (book_id, isbn, title, author)
VALUES (2, '873526739728251', 'Spring Start Here', 'Laurentiu Spilca');
