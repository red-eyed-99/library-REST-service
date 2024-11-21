package dto.author;

import entities.Book;

import java.util.Set;

public class AuthorResponseDTO {

    private final Long id;
    private final String firstName;
    private final String lastName;

    private final Set<Book> books;

    public AuthorResponseDTO(Long id, String firstName, String lastName, Set<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }
}
