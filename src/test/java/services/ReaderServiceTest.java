package services;

import dao.BookDAO;
import dao.ReaderDAO;
import dto.reader.CreateReaderRequestDTO;
import dto.reader.UpdateReaderPhoneDTO;
import entities.Book;
import entities.Reader;
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
class ReaderServiceTest {

    @Mock
    private ReaderDAO readerDAO;

    @Mock
    private BookDAO bookDAO;

    @InjectMocks
    private ReaderService readerService;

    @Test
    @DisplayName("Create reader")
    void createReader_shouldReturnCreateReaderResponseDTO() throws SQLException {
        var createReaderRequestDTO = new CreateReaderRequestDTO("dummy", "dummy", "dummy");

        var createReaderResponseDTO = readerService.create(createReaderRequestDTO);

        verify(readerDAO).create(any(Reader.class));

        assertAll(
                () -> assertNotNull(createReaderResponseDTO),
                () -> assertEquals(createReaderRequestDTO.getFirstName(), createReaderResponseDTO.getFirstName()),
                () -> assertEquals(createReaderRequestDTO.getLastName(), createReaderResponseDTO.getLastName()),
                () -> assertEquals(createReaderRequestDTO.getPhone(), createReaderResponseDTO.getPhone())
        );
    }

    @Test
    @DisplayName("Find all readers")
    void findAll_shouldReturnReaderResponseDTOList() throws SQLException {
        var readerResponseDTOList = readerService.findAll();

        verify(readerDAO).findAll();

        assertNotNull(readerResponseDTOList);
    }

    @Test
    @DisplayName("Find reader by id")
    void findById_shouldReturnReaderResponseDTO() throws SQLException {
        var reader = new Reader.ReaderBuilder(1L, "dummy").build();

        doReturn(reader).when(readerDAO).findById(reader.getId());

        var readerResponseDTO = readerService.findById(reader.getId());

        verify(readerDAO).findById(reader.getId());

        assertAll(
                () -> assertNotNull(readerResponseDTO),
                () -> assertEquals(reader.getId(), readerResponseDTO.getId())
        );
    }

    @Test
    @DisplayName("Update reader phone")
    void updateReaderPhone_shouldReturnUpdateReaderPhoneDTO() throws SQLException {
        var actualDTO = new UpdateReaderPhoneDTO(1L, "dummy");

        var expectedDTO = readerService.updatePhone(actualDTO);

        verify(readerDAO).updatePhone(any(Reader.class));

        assertEquals(actualDTO, expectedDTO);
    }

    @Test
    @DisplayName("Delete reader by id")
    void deleteReaderById_readerDAODeleteMethodInvoked() throws SQLException {
        readerService.delete(anyLong());

        verify(readerDAO).delete(anyLong());
    }

    @Test
    @DisplayName("Add book to reader")
    void addBookToReader_shouldReturnReaderResponseDTO() throws SQLException {
        var book = new Book.BookBuilder("dummy", 1111)
                .setId(1L)
                .build();

        var reader = new Reader.ReaderBuilder(1L, "dummy")
                .setBooks(new HashSet<>())
                .build();

        doReturn(book).when(bookDAO).findById(book.getId());
        doReturn(reader).when(readerDAO).findById(reader.getId());

        var readerResponseDTO = readerService.addBookToReader(reader.getId(), book.getId());

        verify(readerDAO).addBookToReader(anyLong(), anyLong());

        assertTrue(readerResponseDTO.getBooks().contains(book));
    }
}
