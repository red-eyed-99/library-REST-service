package dao;

import entities.Book;
import entities.Reader;
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

public class ReaderDAO {

    private static final String READER_NOT_FOUND_MESSAGE = "Reader not found";

    private final DataSource dataSource;

    public ReaderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(Reader reader) throws SQLException {
        var sql = "INSERT INTO readers (first_name, last_name, phone) VALUES (?, ?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (readerExists(reader.getPhone(), connection)) {
                throw new AlreadyExistException("Reader already exists");
            }

            preparedStatement.setString(1, reader.getFirstName());
            preparedStatement.setString(2, reader.getLastName());
            preparedStatement.setString(3, reader.getPhone());

            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    reader.setId(resultSet.getLong(1));
                }
            }
        }
    }

    public List<Reader> findAll() throws SQLException {
        var sql = "SELECT * FROM readers";

        var readers = new ArrayList<Reader>();

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                var reader = getReader(resultSet);

                readers.add(reader);
            }

            if (readers.isEmpty()) {
                throw new NotFoundException("Readers not found");
            }
        }

        return readers;
    }

    public Reader findById(Long id) throws SQLException {
        var sql = "SELECT * FROM readers WHERE id = ?";

        Reader reader = null;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    reader = getReader(resultSet);
                }
            }
        }

        if (reader == null) {
            throw new NotFoundException(READER_NOT_FOUND_MESSAGE);
        }

        return reader;
    }

    private Reader getReader(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var firstName = resultSet.getString("first_name");
        var lastName = resultSet.getString("last_name");
        var phone = resultSet.getString("phone");

        var books = getReaderBooks(id);

        return new Reader.ReaderBuilder(firstName, lastName, phone)
                .setId(id)
                .setBooks(books)
                .build();
    }

    private Set<Book> getReaderBooks(Long id) throws SQLException {
        var sql = """
                  SELECT id, title, publish_year
                  FROM books JOIN readers_books rb ON books.id = rb.book_id
                  WHERE reader_id = ?
                """;

        var books = new HashSet<Book>();

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
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

    public void updatePhone(Reader reader) throws SQLException {
        var sql = """
                UPDATE readers
                SET phone = ?
                WHERE id = ?
                """;

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            if (!readerExists(reader.getId(), connection)) {
                throw new NotFoundException(READER_NOT_FOUND_MESSAGE);
            }

            if (readerExists(reader.getPhone(), connection)) {
                throw new AlreadyExistException("This number is already taken");
            }

            preparedStatement.setString(1, reader.getPhone());
            preparedStatement.setLong(2, reader.getId());

            preparedStatement.executeUpdate();

            reader.setBooks(getReaderBooks(reader.getId()));
        }
    }

    private boolean readerExists(Long id, Connection connection) throws SQLException {
        var sql = "SELECT id FROM readers WHERE id = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private boolean readerExists(String phone, Connection connection) throws SQLException {
        var sql = "SELECT id FROM readers WHERE phone = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, phone);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void delete(Long id) throws SQLException {
        var sql = "DELETE FROM readers WHERE id = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            if (!readerExists(id, connection)) {
                throw new NotFoundException(READER_NOT_FOUND_MESSAGE);
            }

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public void addBookToReader(Long readerId, Long bookId) throws SQLException {

        if (bookExists(readerId, bookId)) {
            throw new AlreadyExistException("Reader already has this book");
        }

        var sql = "INSERT INTO readers_books(reader_id, book_id) VALUES(?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, readerId);
            preparedStatement.setLong(2, bookId);

            preparedStatement.executeUpdate();
        }
    }

    private boolean bookExists(Long readerId, Long bookId) throws SQLException {
        var sql = "SELECT * FROM readers_books WHERE reader_id = ? AND book_id = ?";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, readerId);
            preparedStatement.setLong(2, bookId);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
