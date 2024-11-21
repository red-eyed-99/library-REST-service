package dto.book;

import java.util.Set;

public class CreateBookDTO {

    private final String title;
    private final int publishYear;

    private final Set<Long> authorsId;

    public CreateBookDTO(String title, int publishYear, Set<Long> authorsId) {
        this.title = title;
        this.publishYear = publishYear;
        this.authorsId = authorsId;
    }

    public String getTitle() {
        return title;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public Set<Long> getAuthorsId() {
        return authorsId;
    }
}
