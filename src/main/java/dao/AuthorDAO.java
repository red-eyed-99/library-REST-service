package dao;

import entities.Author;
import entities.Book;
import exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthorDAO {

    private static final String AUTHOR_NOT_FOUND_MESSAGE = "Author not found";

    private final DataSource dataSource;

    public AuthorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(Author author) throws SQLException {
        var sql = "INSERT INTO authors (first_name, last_name) VALUES (?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());

            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    author.setId(resultSet.getLong(1));
                }
            }
        }
    }

    public Author findById(Long id) throws SQLException {
        var sql = "SELECT * FROM authors WHERE id = ?";

        Author author = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    author = getAuthor(resultSet, connection);
                }
            }
        }

        if (author == null) {
            throw new NotFoundException(AUTHOR_NOT_FOUND_MESSAGE);
        }

        return author;
    }

    public List<Author> findAll() throws SQLException {
        var sql = "SELECT * FROM authors";

        var authors = new ArrayList<Author>();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                var author = getAuthor(resultSet, connection);

                authors.add(author);
            }

            if (authors.isEmpty()) {
                throw new NotFoundException("Authors not found");
            }
        }

        return authors;
    }

    private Author getAuthor(ResultSet resultSet, Connection connection) throws SQLException {
        var id = resultSet.getLong("id");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");

        return new Author.AuthorBuilder(firstName, lastName)
                .setId(id)
                .setBooks(getAuthorBooks(id, connection))
                .build();
    }

    private Set<Book> getAuthorBooks(Long authorId, Connection connection) throws SQLException {
        var sql = """
                  SELECT id, title, publish_year
                  FROM books JOIN authors_books ab ON books.id = ab.book_id
                  WHERE author_id = ?
                """;

        var books = new HashSet<Book>();

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, authorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var bookId = resultSet.getLong("id");
                    var title = resultSet.getString("title");
                    var publishYear = resultSet.getInt("publish_year");

                    var book = new Book.BookBuilder(title, publishYear)
                            .setId(bookId)
                            .build();

                    books.add(book);
                }
            }
        }

        return books;
    }

    public void update(Author author) throws SQLException {
        var sql = """
                  UPDATE authors
                  SET first_name = ?, last_name = ?
                  WHERE id = ?
                """;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            if (!authorExists(author.getId(), connection)) {
                throw new NotFoundException(AUTHOR_NOT_FOUND_MESSAGE);
            }

            preparedStatement.setString(1, author.getFirstName());
            preparedStatement.setString(2, author.getLastName());
            preparedStatement.setLong(3, author.getId());

            preparedStatement.executeUpdate();

            author.setBooks(getAuthorBooks(author.getId(), connection));
        }
    }

    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM authors WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            if (!authorExists(id, connection)) {
                throw new NotFoundException(AUTHOR_NOT_FOUND_MESSAGE);
            }

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        }
    }

    private boolean authorExists(Long id, Connection connection) throws SQLException {
        var sql = "SELECT id FROM authors WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
