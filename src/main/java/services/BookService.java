package services;

import dao.AuthorDAO;
import dao.BookDAO;
import dto.book.BookResponseDTO;
import dto.book.CreateBookDTO;
import entities.Author;
import entities.Book;
import utils.mappers.BookMapper;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookService {

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    public BookService(BookDAO bookDAO, AuthorDAO authorDAO) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
    }

    public BookResponseDTO create(CreateBookDTO createBookDTO) throws SQLException {
        var title = createBookDTO.getTitle();
        var publishYear = createBookDTO.getPublishYear();

        var book = new Book.BookBuilder(title, publishYear).build();

        var authors = findAuthors(createBookDTO);

        book.setAuthors(authors);

        bookDAO.create(book);

        addBookToAuthors(book, authors);

        return bookMapper.toResponseDTO(book);
    }

    public List<BookResponseDTO> findAll() throws SQLException {
        List<Book> books = bookDAO.findAll();

        return bookMapper.toResponseDTOList(books);
    }

    public BookResponseDTO findById(Long id) throws SQLException {
        var book = bookDAO.findById(id);

        return bookMapper.toResponseDTO(book);
    }

    public void delete(Long id) throws SQLException {
        bookDAO.delete(id);
    }

    private Set<Author> findAuthors(CreateBookDTO createBookDTO) throws SQLException {
        var authors = new HashSet<Author>();

        for (var authorId : createBookDTO.getAuthorsId()) {
            var author = authorDAO.findById(authorId);
            authors.add(author);
        }

        return authors;
    }

    private void addBookToAuthors(Book book, Set<Author> authors) throws SQLException {
        for (var author : authors) {
            bookDAO.addBookToAuthor(book.getId(), author.getId());
        }
    }
}
