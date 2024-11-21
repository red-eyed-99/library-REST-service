package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.Objects;

public class Review {

    private Long id;

    private Reader reader;

    @JsonIgnore
    private Book book;

    private String content;
    private LocalDate date;

    private Review(Review.ReviewBuilder reviewBuilder) {
        id = reviewBuilder.id;
        reader = reviewBuilder.reader;
        book = reviewBuilder.book;
        content = reviewBuilder.content;
        date = reviewBuilder.date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static class ReviewBuilder {
        private Long id;

        private Reader reader;
        private Book book;

        private String content;

        private LocalDate date;

        public ReviewBuilder(Reader reader, String content) {
            this.reader = reader;
            this.content = content;
        }

        public ReviewBuilder(Reader reader, Book book, String content) {
            this.reader = reader;
            this.book = book;
            this.content = content;
        }

        public Review.ReviewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public Review.ReviewBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
