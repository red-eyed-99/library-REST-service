package services;

import dao.BookDAO;
import dao.ReaderDAO;
import dao.ReviewDAO;
import dto.book.BookReviewDTO;
import dto.review.CreateReviewDTO;
import dto.review.ReviewResponseDTO;
import entities.Review;
import utils.mappers.ReviewMapper;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {

    private final ReviewDAO reviewDAO;
    private final ReaderDAO readerDAO;
    private final BookDAO bookDAO;

    private final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;

    public ReviewService(ReviewDAO reviewDAO, ReaderDAO readerDAO, BookDAO bookDAO) {
        this.reviewDAO = reviewDAO;
        this.readerDAO = readerDAO;
        this.bookDAO = bookDAO;
    }

    public ReviewResponseDTO create(CreateReviewDTO createReviewDTO) throws SQLException {
        var reader = readerDAO.findById(createReviewDTO.getReaderId());
        var book = bookDAO.findById(createReviewDTO.getBookId());
        var content = createReviewDTO.getContent();

        var review = new Review.ReviewBuilder(reader, book, content).build();

        reviewDAO.create(review);

        return reviewMapper.toResponseDTO(review);
    }

    public List<BookReviewDTO> findAll(Long bookId) throws SQLException {
        List<Review> reviews = reviewDAO.findAll(bookId);

        return reviewMapper.toResponseDTOList(reviews);
    }
}
