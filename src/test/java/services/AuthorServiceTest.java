package services;

import dao.AuthorDAO;
import dto.author.CreateAuthorRequestDTO;
import dto.author.UpdateAuthorNameDTO;
import entities.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorDAO authorDAO;

    @InjectMocks
    private AuthorService authorService;

    @Test
    @DisplayName("Create author")
    void createAuthor_shouldReturnCreateAuthorResponseDTO() throws SQLException {
        var createAuthorRequestDTO = new CreateAuthorRequestDTO("dummy", "dummy");

        var createAuthorResponseDTO = authorService.create(createAuthorRequestDTO);

        verify(authorDAO).create(any(Author.class));

        assertAll(
                () -> assertNotNull(createAuthorResponseDTO),
                () -> assertEquals(createAuthorRequestDTO.getFirstName(), createAuthorResponseDTO.getFirstName()),
                () -> assertEquals(createAuthorRequestDTO.getLastName(), createAuthorResponseDTO.getLastName())
        );
    }

    @Test
    @DisplayName("Find all authors")
    void findAll_shouldReturnAuthorResponseDTOList() throws SQLException {
        var authorResponseDTOList = authorService.findAll();

        verify(authorDAO).findAll();

        assertNotNull(authorResponseDTOList);
    }

    @Test
    @DisplayName("Find author by id")
    void findById_shouldReturnAuthorResponseDTO() throws SQLException {
        var author = new Author.AuthorBuilder("dummy", "dummy")
                .setId(1L)
                .build();

        doReturn(author).when(authorDAO).findById(author.getId());

        var authorResponseDTO = authorService.findById(author.getId());

        verify(authorDAO).findById(author.getId());

        assertAll(
                () -> assertNotNull(authorResponseDTO),
                () -> assertEquals(author.getId(), authorResponseDTO.getId())
        );
    }

    @Test
    @DisplayName("Update author name")
    void updateAuthorName_shouldReturnUpdateNameAuthorNameDTO() throws SQLException {
        var actualDTO = new UpdateAuthorNameDTO(1L, "dummy", "dummy");

        var expectedDTO = authorService.updateName(actualDTO);

        verify(authorDAO).update(any(Author.class));

        assertEquals(actualDTO, expectedDTO);
    }

    @Test
    @DisplayName("Delete author by id")
    void deleteAuthorById_authorDAODeleteMethodInvoked() throws SQLException {
        authorService.delete(anyLong());

        verify(authorDAO).delete(anyLong());
    }
}
