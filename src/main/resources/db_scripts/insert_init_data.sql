-- Authors --
INSERT INTO authors (first_name, last_name) VALUES ('Paola', 'Shilston');
INSERT INTO authors (first_name, last_name) VALUES ('Sam', 'Grigg');
INSERT INTO authors (first_name, last_name) VALUES ('Ezra', 'Brankley');
INSERT INTO authors (first_name, last_name) VALUES ('Helaine', 'Abatelli');
INSERT INTO authors (first_name, last_name) VALUES ('Fey', 'Seedman');
INSERT INTO authors (first_name, last_name) VALUES ('Gerard', 'Iacovides');
INSERT INTO authors (first_name, last_name) VALUES ('Faustina', 'Hanretty');
INSERT INTO authors (first_name, last_name) VALUES ('Dorothy', 'Elston');
INSERT INTO authors (first_name, last_name) VALUES ('Alano', 'Condit');
INSERT INTO authors (first_name, last_name) VALUES ('Tannie', 'Briand');

-- Readers --
INSERT INTO readers (first_name, last_name, phone) VALUES ('Jenni', 'Cardo', '+7(253)-670-12-13');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Janella', 'Teenan', '+7(096)-628-35-53');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Maritsa', 'Wolfer', '+7(205)-457-27-67');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Lovell', 'Dengate', '+7(768)-553-52-57');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Irv', 'Mell', '+7(485)-809-63-57');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Sherline', 'Clemmow', '+7(811)-288-62-96');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Jacinthe', 'Demougeot', '+7(995)-693-46-16');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Daile', 'Youngman', '+7(267)-743-30-22');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Vernor', 'Sherwin', '+7(587)-849-59-12');
INSERT INTO readers (first_name, last_name, phone) VALUES ('Sancho', 'Olanda', '+7(970)-050-53-50');

-- Books --
INSERT INTO books (title, publish_year) VALUES ('Helicopter String Quartet', 1996);
INSERT INTO books (title, publish_year) VALUES ('Statement, The', 2012);
INSERT INTO books (title, publish_year) VALUES ('Betrayal', 2002);
INSERT INTO books (title, publish_year) VALUES ('Thief Who Came to Dinner, The', 2012);
INSERT INTO books (title, publish_year) VALUES ('A Year Along the Abandoned Road', 2007);
INSERT INTO books (title, publish_year) VALUES ('Straight to Hell', 1999);
INSERT INTO books (title, publish_year) VALUES ('Missing Person, The', 2002);
INSERT INTO books (title, publish_year) VALUES ('Blackrock', 2002);
INSERT INTO books (title, publish_year) VALUES ('Dancing in September', 2008);
INSERT INTO books (title, publish_year) VALUES ('Robin Williams: Weapons of Self Destruction', 1990);

-- Authors books --
INSERT INTO authors_books (author_id, book_id) VALUES (6, 8);
INSERT INTO authors_books (author_id, book_id) VALUES (2, 8);
INSERT INTO authors_books (author_id, book_id) VALUES (9, 2);
INSERT INTO authors_books (author_id, book_id) VALUES (5, 10);
INSERT INTO authors_books (author_id, book_id) VALUES (4, 3);
INSERT INTO authors_books (author_id, book_id) VALUES (6, 2);
INSERT INTO authors_books (author_id, book_id) VALUES (1, 10);
INSERT INTO authors_books (author_id, book_id) VALUES (6, 3);
INSERT INTO authors_books (author_id, book_id) VALUES (1, 9);
INSERT INTO authors_books (author_id, book_id) VALUES (5, 9);
INSERT INTO authors_books (author_id, book_id) VALUES (1, 1);
INSERT INTO authors_books (author_id, book_id) VALUES (5, 1);
INSERT INTO authors_books (author_id, book_id) VALUES (10, 9);
INSERT INTO authors_books (author_id, book_id) VALUES (8, 8);
INSERT INTO authors_books (author_id, book_id) VALUES (7, 1);
INSERT INTO authors_books (author_id, book_id) VALUES (4, 6);
INSERT INTO authors_books (author_id, book_id) VALUES (10, 8);
INSERT INTO authors_books (author_id, book_id) VALUES (7, 5);
INSERT INTO authors_books (author_id, book_id) VALUES (1, 7);
INSERT INTO authors_books (author_id, book_id) VALUES (3, 9);

-- Readers books --
INSERT INTO readers_books (reader_id, book_id) VALUES (2, 7);
INSERT INTO readers_books (reader_id, book_id) VALUES (7, 2);
INSERT INTO readers_books (reader_id, book_id) VALUES (8, 8);
INSERT INTO readers_books (reader_id, book_id) VALUES (6, 6);
INSERT INTO readers_books (reader_id, book_id) VALUES (9, 10);
INSERT INTO readers_books (reader_id, book_id) VALUES (10, 9);
INSERT INTO readers_books (reader_id, book_id) VALUES (4, 2);
INSERT INTO readers_books (reader_id, book_id) VALUES (1, 10);
INSERT INTO readers_books (reader_id, book_id) VALUES (6, 1);
INSERT INTO readers_books (reader_id, book_id) VALUES (4, 8);
INSERT INTO readers_books (reader_id, book_id) VALUES (9, 2);
INSERT INTO readers_books (reader_id, book_id) VALUES (3, 10);
INSERT INTO readers_books (reader_id, book_id) VALUES (4, 1);
INSERT INTO readers_books (reader_id, book_id) VALUES (1, 7);
INSERT INTO readers_books (reader_id, book_id) VALUES (6, 7);
INSERT INTO readers_books (reader_id, book_id) VALUES (3, 3);
INSERT INTO readers_books (reader_id, book_id) VALUES (6, 5);
INSERT INTO readers_books (reader_id, book_id) VALUES (5, 8);
INSERT INTO readers_books (reader_id, book_id) VALUES (6, 8);
INSERT INTO readers_books (reader_id, book_id) VALUES (5, 4);

-- Reviews --
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (1, 3, 'Interesting and well-written book.', '2024-01-12');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (2, 5, 'A bit slow at the start but great finish!', '2024-02-14');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (3, 7, 'Didn’t live up to the hype.', '2024-03-10');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (4, 2, 'Highly recommended for historical fiction lovers.', '2024-04-01');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (5, 9, 'A deep and thought-provoking read.', '2024-05-19');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (6, 8, 'Not what I expected, but a pleasant surprise.', '2024-06-08');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (7, 1, 'The characters were well developed.', '2024-07-21');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (8, 4, 'The plot twists were predictable.', '2024-08-16');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (9, 6, 'A page-turner from start to finish!', '2024-09-07');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (10, 10, 'Too long and dragged in the middle.', '2024-10-22');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (5, 2, 'Loved the unique narrative style.', '2024-01-01');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (3, 1, 'A compelling read with lots of depth.', '2024-02-15');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (2, 10, 'Couldn’t put it down!', '2024-03-03');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (9, 4, 'The world-building was fascinating.', '2024-04-17');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (9, 1, 'I found the protagonist unrelatable.', '2024-05-05');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (7, 5, 'A modern classic.', '2024-06-29');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (1, 6, 'Too many subplots, got confusing.', '2024-07-12');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (3, 9, 'Absolutely loved the writing style.', '2024-08-09');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (4, 10, 'Would recommend for fans of the genre.', '2024-09-11');
INSERT INTO reviews (reader_id, book_id, content, date) VALUES (2, 1, 'Disappointing ending, but a good read overall.', '2024-10-30');
