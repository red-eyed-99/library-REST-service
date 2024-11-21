package dto.review;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import entities.Book;
import entities.Reader;

import java.time.LocalDate;

public class ReviewResponseDTO {

    private final Long id;

    private final Reader reader;
    private final Book book;

    private final String content;

    @JsonSerialize(using = LocalDateSerializer.class)
    private final LocalDate date;

    public ReviewResponseDTO(Long id, Reader reader, Book book, String content, LocalDate date) {
        this.id = id;
        this.reader = reader;
        this.book = book;
        this.content = content;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Reader getReader() {
        return reader;
    }

    public Book getBook() {
        return book;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getDate() {
        return date;
    }
}
