package utils.mappers;

import dto.author.AuthorResponseDTO;
import dto.author.CreateAuthorResponseDTO;
import entities.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorResponseDTO toResponseDTO(Author author);

    CreateAuthorResponseDTO toCreateResponseDTO(Author author);

    List<AuthorResponseDTO> toResponseDTOList(List<Author> authors);
}
