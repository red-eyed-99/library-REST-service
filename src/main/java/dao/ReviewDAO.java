package dao;

import entities.Review;
import exceptions.AlreadyExistException;
import exceptions.NotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private final DataSource dataSource;

    private final ReaderDAO readerDAO;

    public ReviewDAO(DataSource dataSource, ReaderDAO readerDAO) {
        this.dataSource = dataSource;
        this.readerDAO = readerDAO;
    }

    public void create(Review review) throws SQLException {
        var reader = review.getReader();
        var book = review.getBook();

        var sql = "INSERT INTO reviews (reader_id, book_id, content, date) VALUES (?, ?, ?, ?)";

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (reviewExists(reader.getId(), book.getId(), connection)) {
                throw new AlreadyExistException("There is already a review for this book");
            }

            preparedStatement.setLong(1, reader.getId());
            preparedStatement.setLong(2, book.getId());
            preparedStatement.setString(3, review.getContent());

            var currentDate = LocalDate.now();

            preparedStatement.setDate(4, Date.valueOf(currentDate));

            preparedStatement.executeUpdate();

            try (var resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    review.setId(resultSet.getLong(1));
                    review.setDate(currentDate);
                }
            }
        }
    }

    public List<Review> findAll(Long bookId) throws SQLException {
        var sql = "SELECT id, reader_id, content, date FROM reviews WHERE book_id = ?";

        var reviews = new ArrayList<Review>();

        try (var connection = dataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, bookId);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    var reviewId = resultSet.getLong("id");
                    var reader = readerDAO.findById(resultSet.getLong("reader_id"));
                    var content = resultSet.getString("content");
                    var date = resultSet.getDate("date").toLocalDate();

                    var review = new Review.ReviewBuilder(reader, content)
                            .setId(reviewId)
                            .setDate(date)
                            .build();

                    reviews.add(review);
                }
            }
        }

        if (reviews.isEmpty()) {
            throw new NotFoundException("Reviews not found");
        }

        return reviews;
    }

    private boolean reviewExists(Long readerId, Long book_id, Connection connection) throws SQLException {
        var sql = "SELECT * FROM reviews WHERE reader_id = ? AND book_id = ?";

        try (var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, readerId);
            preparedStatement.setLong(2, book_id);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
