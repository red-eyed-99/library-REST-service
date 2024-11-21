package dao;

import entities.Author;
import entities.Book;
import exceptions.AlreadyExistException;
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

public class BookDAO {

    private static final String BOOK_NOT_FOUND_MESSAGE = "Book not found";

    private final DataSource dataSource;

    public BookDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, publish_year) VALUES (?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (bookExists(book, connection)) {
                throw new AlreadyExistException("Book already exists");
            }

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getPublishYear());

            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    book.setId(resultSet.getLong(1));
                }
            }
        }
    }

    public void addBookToAuthor(Long bookId, Long authorId) throws SQLException {
        var sql = "INSERT INTO authors_books (author_id, book_id) VALUES (?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, authorId);
            preparedStatement.setLong(2, bookId);

            preparedStatement.executeUpdate();
        }
    }

    public List<Book> findAll() throws SQLException {
        var sql = "SELECT * FROM books";

        var books = new ArrayList<Book>();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                var book = getBook(resultSet, connection);

                books.add(book);
            }

            if (books.isEmpty()) {
                throw new NotFoundException("Books not found");
            }
        }

        return books;
    }

    public Book findById(Long id) throws SQLException {
        var sql = "SELECT * FROM books WHERE id = ?";

        Book book = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql);) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    book = getBook(resultSet, connection);
                }
            }
        }

        if (book == null) {
            throw new NotFoundException(BOOK_NOT_FOUND_MESSAGE);
        }

        return book;
    }

    private Book getBook(ResultSet resultSet, Connection connection) throws SQLException {
        var id = resultSet.getLong("id");
        var title = resultSet.getString("title");
        var publishYear = resultSet.getInt("publish_year");

        var authors = getBookAuthors(id, connection);

        return new Book.BookBuilder(title, publishYear)
                .setId(id)
                .setAuthors(authors)
                .build();
    }

    private Set<Author> getBookAuthors(Long bookId, Connection connection) throws SQLException {
        var sql = """
                  SELECT id, first_name, last_name
                  FROM authors JOIN authors_books ab ON authors.id = ab.author_id
                  WHERE book_id = ?
                """;

        var authors = new HashSet<Author>();

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, bookId);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var authorId = resultSet.getLong("id");
                    var firstName = resultSet.getString("first_name");
                    var lastName = resultSet.getString("last_name");

                    var author = new Author.AuthorBuilder(firstName, lastName)
                            .setId(authorId)
                            .build();

                    authors.add(author);
                }
            }
        }

        return authors;
    }

    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM books WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            if (!bookExists(id, connection)) {
                throw new NotFoundException(BOOK_NOT_FOUND_MESSAGE);
            }

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        }
    }

    private boolean bookExists(Long id, Connection connection) throws SQLException {
        var sql = "SELECT id FROM books WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean bookExists(Book book, Connection connection) throws SQLException {
        var sql = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(?) AND publish_year = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setInt(2, book.getPublishYear());

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var bookAuthors = getBookAuthors(resultSet.getLong("id"), connection);

                    if (bookAuthors.containsAll(book.getAuthors())) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
