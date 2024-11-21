CREATE TABLE books
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    publish_year VARCHAR(4) NOT NULL
);

CREATE TABLE authors
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(30) NOT NULL
);

CREATE TABLE authors_books
(
    author_id BIGINT,
    book_id   BIGINT,

    FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    PRIMARY KEY (author_id, book_id)
);

CREATE TABLE readers
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name  VARCHAR(30) NOT NULL,
    phone      VARCHAR(17) NOT NULL UNIQUE,

    CONSTRAINT check_phone CHECK (phone REGEXP '^\\+7\\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}$')
);

CREATE TABLE readers_books
(
    reader_id BIGINT,
    book_id   BIGINT,

    FOREIGN KEY (reader_id) REFERENCES readers (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE,
    PRIMARY KEY (reader_id, book_id)
);

CREATE TABLE reviews
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reader_id BIGINT,
    book_id BIGINT,
    content VARCHAR(500) NOT NULL,
    date DATE NOT NULL,

    FOREIGN KEY (reader_id) REFERENCES readers (id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE
)