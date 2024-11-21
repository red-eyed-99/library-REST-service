package services;

import dao.AuthorDAO;
import dao.BookDAO;
import dto.book.CreateBookDTO;
import entities.Author;
import entities.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private AuthorDAO authorDAO;

    @InjectMocks
    private BookService bookService;

    @Test
    @DisplayName("Create book")
    void createBook_shouldReturnBookResponseDTO() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy")
                .setId(1L)
                .build();

        var authorsId = new HashSet<Long>();
        authorsId.add(author.getId());

        var createBookDTO = new CreateBookDTO("dummy", 1111, authorsId);

        doReturn(author).when(authorDAO).findById(author.getId());

        var bookResponseDTO = bookService.create(createBookDTO);

        verify(bookDAO).create(any(Book.class));
        verify(authorDAO).findById(anyLong());
        verify(bookDAO).addBookToAuthor(bookResponseDTO.getId(), author.getId());

        assertAll(
                () -> assertNotNull(bookResponseDTO),
                () -> assertEquals(createBookDTO.getTitle(), bookResponseDTO.getTitle()),
                () -> assertFalse(bookResponseDTO.getAuthors().isEmpty())
        );
    }

    @Test
    @DisplayName("Find all books")
    void findAll_shouldReturnBookResponseDTOList() throws SQLException {
        var bookResponseDTOList = bookService.findAll();

        verify(bookDAO).findAll();

        assertNotNull(bookResponseDTOList);
    }

    @Test
    @DisplayName("Find book by id")
    void findById_shouldReturnBookResponseDTO() throws SQLException {
        var book = new Book.BookBuilder("dummy", 1111)
                .setId(1L)
                .build();

        doReturn(book).when(bookDAO).findById(book.getId());

        var bookResponseDTO = bookService.findById(book.getId());

        verify(bookDAO).findById(book.getId());

        assertAll(
                () -> assertNotNull(bookResponseDTO),
                () -> assertEquals(book.getId(), bookResponseDTO.getId())
        );
    }

    @Test
    @DisplayName("Delete book by id")
    void deleteBookById_bookDAODeleteMethodInvoked() throws SQLException {
        bookService.delete(anyLong());

        verify(bookDAO).delete(anyLong());
    }
}
