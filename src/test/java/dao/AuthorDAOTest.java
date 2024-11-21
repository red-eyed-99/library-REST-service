package dao;

import entities.Author;
import exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDAOTest extends BaseDAOTest {

    @Test
    @DisplayName("Create author")
    void shouldAddAuthorToDatabase() throws SQLException {
        var expectedAuthor = new Author.AuthorBuilder("dummy", "dummy").build();

        authorDAO.create(expectedAuthor);

        assertAll(
                () -> assertNotNull(expectedAuthor.getId()),
                () -> assertEquals(expectedAuthor, authorDAO.findById(expectedAuthor.getId()))
        );
    }

    @Test
    @DisplayName("Find all authors")
    void shouldReturnAllAuthorsFromDatabase() throws SQLException {
        var expectedAuthors = new HashSet<Author>();

        for (int i = 0; i < 3; i++) {
            var author = new Author.AuthorBuilder("dummy", "dummy").build();

            expectedAuthors.add(author);

            authorDAO.create(author);
        }

        var authors = authorDAO.findAll();

        assertTrue(authors.containsAll(expectedAuthors));
    }

    @Test
    @DisplayName("Update author name")
    void shouldUpdateAuthorNameInDatabase() throws SQLException {
        var author = new Author.AuthorBuilder("Ivan", "Ivan").build();

        authorDAO.create(author);

        author.setFirstName("Petr");
        author.setLastName("Petrov");

        authorDAO.update(author);

        var updatedAuthor = authorDAO.findById(author.getId());

        assertAll(
                () -> assertEquals(author.getId(), updatedAuthor.getId()),
                () -> assertEquals("Petr", updatedAuthor.getFirstName()),
                () -> assertEquals("Petrov", updatedAuthor.getLastName())
        );
    }

    @Test
    @DisplayName("Delete author by id")
    void shouldDeleteAuthorFromDatabase() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy").build();

        authorDAO.create(author);
        authorDAO.delete(author.getId());

        assertThrows(NotFoundException.class, () -> authorDAO.findById(author.getId()));
    }
}
