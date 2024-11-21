package dao;

import entities.Author;
import entities.Book;
import exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BookDAOTest extends BaseDAOTest {

    @Test
    @DisplayName("Create book")
    void shouldAddBookToDatabase() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy").build();
        var expectedBook = new Book.BookBuilder("dummy", 1111)
                .setAuthors(Set.of(author))
                .build();

        bookDAO.create(expectedBook);

        assertAll(
                () -> assertNotNull(expectedBook.getId()),
                () -> assertEquals(expectedBook, bookDAO.findById(expectedBook.getId()))
        );
    }

    @Test
    @DisplayName("Add book to author")
    void shouldAddBookToAuthorInDatabase() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy").build();

        authorDAO.create(author);

        var book = new Book.BookBuilder("dummy", 1111)
                .setAuthors(Set.of(author))
                .build();

        bookDAO.create(book);

        bookDAO.addBookToAuthor(book.getId(), author.getId());

        assertTrue(authorDAO.findById(author.getId())
                .getBooks()
                .contains(book));
    }

    @Test
    @DisplayName("Find all books")
    void shouldReturnAllBooksFromDatabase() throws SQLException {
        var expectedBooks = new HashSet<Book>();

        for (int i = 0; i < 3; i++) {
            var author = new Author.AuthorBuilder("dummy", "dummy").build();

            authorDAO.create(author);

            var book = new Book.BookBuilder("dummy", 1111)
                    .setAuthors(Set.of(author))
                    .build();

            expectedBooks.add(book);

            bookDAO.create(book);
        }

        var books = bookDAO.findAll();

        assertTrue(books.containsAll(expectedBooks));
    }

    @Test
    @DisplayName("Delete book by id")
    void shouldDeleteAuthorFromDatabase() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy").build();
        var book = new Book.BookBuilder("dummy", 1111)
                .setAuthors(Set.of(author))
                .build();

        bookDAO.create(book);
        bookDAO.delete(book.getId());

        assertThrows(NotFoundException.class, () -> bookDAO.findById(book.getId()));
    }
}
