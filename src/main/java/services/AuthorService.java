package services;

import dao.AuthorDAO;
import dto.author.AuthorResponseDTO;
import dto.author.CreateAuthorRequestDTO;
import dto.author.CreateAuthorResponseDTO;
import dto.author.UpdateAuthorNameDTO;
import entities.Author;
import utils.mappers.AuthorMapper;

import java.sql.SQLException;
import java.util.List;

public class AuthorService {

    private final AuthorDAO authorDAO;

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    public CreateAuthorResponseDTO create(CreateAuthorRequestDTO createAuthorRequestDTO) throws SQLException {
        var authorFirstName = createAuthorRequestDTO.getFirstName();
        var authorLastName = createAuthorRequestDTO.getLastName();

        var author = new Author.AuthorBuilder(authorFirstName, authorLastName).build();

        authorDAO.create(author);

        return authorMapper.toCreateResponseDTO(author);
    }

    public List<AuthorResponseDTO> findAll() throws SQLException {
        List<Author> authors = authorDAO.findAll();

        return authorMapper.toResponseDTOList(authors);
    }

    public AuthorResponseDTO findById(Long id) throws SQLException {
        var author = authorDAO.findById(id);

        return authorMapper.toResponseDTO(author);
    }

    public UpdateAuthorNameDTO updateName(UpdateAuthorNameDTO updateAuthorNameDTO) throws SQLException {
        var firstName = updateAuthorNameDTO.getFirstName();
        var lastName = updateAuthorNameDTO.getLastName();
        var id = updateAuthorNameDTO.getId();

        var author = new Author.AuthorBuilder(firstName, lastName)
                .setId(id)
                .build();

        authorDAO.update(author);

        return updateAuthorNameDTO;
    }

    public void delete(Long id) throws SQLException {
        authorDAO.delete(id);
    }
}
