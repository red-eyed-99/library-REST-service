package utils.mappers;

import dto.book.BookReviewDTO;
import dto.review.ReviewResponseDTO;
import entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    ReviewResponseDTO toResponseDTO(Review review);

    List<BookReviewDTO> toResponseDTOList(List<Review> reviews);
}
