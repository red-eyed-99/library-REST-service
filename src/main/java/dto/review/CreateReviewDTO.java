package dto.review;

public class CreateReviewDTO {

    private final Long readerId;
    private final Long bookId;
    private final String content;

    public CreateReviewDTO(Long readerId, Long bookId, String content) {
        this.readerId = readerId;
        this.bookId = bookId;
        this.content = content;
    }

    public Long getReaderId() {
        return readerId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getContent() {
        return content;
    }
}
