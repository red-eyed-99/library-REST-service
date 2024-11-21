package services;

import dao.BookDAO;
import dao.ReaderDAO;
import dao.ReviewDAO;
import dto.reader.CreateReaderRequestDTO;
import dto.review.CreateReviewDTO;
import entities.Book;
import entities.Reader;
import entities.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewDAO reviewDAO;

    @Mock
    private ReaderDAO readerDAO;

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("Create review")
    void createReview_shouldReturnReviewResponseDTO() throws SQLException {
        var reader = new Reader.ReaderBuilder(1L, "dummy").build();

        var book = new Book.BookBuilder("dummy", 1111)
                .setId(1L)
                .build();

        var createReviewDTO = new CreateReviewDTO(reader.getId(), book.getId(), "dummy");

        doReturn(reader).when(readerDAO).findById(reader.getId());
        doReturn(book).when(bookDAO).findById(book.getId());

        var reviewResponseDTO = reviewService.create(createReviewDTO);

        verify(reviewDAO).create(any(Review.class));

        assertAll(
                () -> assertNotNull(reviewResponseDTO),
                () -> assertEquals(reader, reviewResponseDTO.getReader()),
                () -> assertEquals(book, reviewResponseDTO.getBook()),
                () -> assertEquals(createReviewDTO.getContent(), reviewResponseDTO.getContent())
        );
    }

    @Test
    @DisplayName("Find all book reviews")
    void findAll_shouldReturnBookReviewDTOList() throws SQLException {
        var bookReviewDTOList = reviewService.findAll(anyLong());

        verify(reviewDAO).findAll(anyLong());

        assertNotNull(bookReviewDTOList);
    }
}
