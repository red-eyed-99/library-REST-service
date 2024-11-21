package dao;

import entities.Author;
import entities.Book;
import entities.Reader;
import entities.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.RandomPhoneGenerator;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewDAOTest extends BaseDAOTest {

    @Test
    @DisplayName("Create review")
    void shouldAddReviewToDatabase() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy").build();

        authorDAO.create(author);

        var book = new Book.BookBuilder("dummy", 1111)
                .setAuthors(Set.of(author))
                .build();

        bookDAO.create(book);

        var reader = new Reader.ReaderBuilder("dummy", "dummy", RandomPhoneGenerator.generate()).build();

        readerDAO.create(reader);

        var expectedReview = new Review.ReviewBuilder(reader, book, "dummy").build();

        reviewDAO.create(expectedReview);

        assertAll(
                () -> assertNotNull(expectedReview.getId()),
                () -> assertTrue(reviewDAO.findAll(book.getId()).contains(expectedReview))
        );
    }

    @Test
    @DisplayName("Find all reviews by book id")
    void shouldReturnAllBookReviewsFromDatabase() throws SQLException {
        var expectedReviews = new HashSet<Review>();

        var author = new Author.AuthorBuilder("dummy", "dummy").build();

        authorDAO.create(author);

        var book = new Book.BookBuilder("dummy", 1111)
                .setAuthors(Set.of(author))
                .build();

        bookDAO.create(book);

        for (int i = 0; i < 3; i++) {
            var reader = new Reader.ReaderBuilder("dummy", "dummy", RandomPhoneGenerator.generate()).build();

            readerDAO.create(reader);

            var review = new Review.ReviewBuilder(reader, book, "dummy").build();

            expectedReviews.add(review);

            reviewDAO.create(review);
        }

        var reviews = reviewDAO.findAll(book.getId());

        assertTrue(reviews.containsAll(expectedReviews));
    }
}
