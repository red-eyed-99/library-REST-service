package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.Set;

public class Book {

    private Long id;

    private String title;
    private int publishYear;

    @JsonIgnore
    private Set<Author> authors;

    @JsonIgnore
    private Set<Reader> readers;

    private Book(BookBuilder bookBuilder) {
        id = bookBuilder.id;
        title = bookBuilder.title;
        publishYear = bookBuilder.publishYear;
        authors = bookBuilder.authors;
        readers = bookBuilder.readers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Reader> getReaders() {
        return readers;
    }

    public static class BookBuilder {
        private Long id;

        private String title;
        private int publishYear;

        private Set<Author> authors;
        private Set<Reader> readers;

        public BookBuilder(String title, int publishYear) {
            this.title = title;
            this.publishYear = publishYear;
        }

        public BookBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public BookBuilder setAuthors(Set<Author> authors) {
            this.authors = authors;
            return this;
        }

        public BookBuilder setReaders(Set<Reader> readers) {
            this.readers = readers;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
