package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;
import java.util.Set;

public class Reader {

    private Long id;

    private String firstName;
    private String lastName;

    private String phone;

    @JsonIgnore
    private Set<Book> books;

    private Reader(Reader.ReaderBuilder readerBuilder) {
        id = readerBuilder.id;
        firstName = readerBuilder.firstName;
        lastName = readerBuilder.lastName;
        phone = readerBuilder.phone;
        books = readerBuilder.books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public static class ReaderBuilder {
        private Long id;

        private String firstName;
        private String lastName;

        private String phone;

        private Set<Book> books;

        public ReaderBuilder(Long id, String phone) {
            this.id = id;
            this.phone = phone;
        }

        public ReaderBuilder(String firstName, String lastName, String phone) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
        }

        public Reader.ReaderBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public Reader.ReaderBuilder setBooks(Set<Book> books) {
            this.books = books;
            return this;
        }

        public Reader build() {
            return new Reader(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return Objects.equals(id, reader.id) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
