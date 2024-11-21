package dto.book;

import entities.Author;

import java.util.Set;

public class BookResponseDTO {

    private Long id;
    private String title;
    private String publishYear;

    private Set<Author> authors;

    public BookResponseDTO() {}

    public BookResponseDTO(Long id, String title, String publishYear, Set<Author> authors) {
        this.id = id;
        this.title = title;
        this.publishYear = publishYear;
        this.authors = authors;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
