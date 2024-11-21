package utils.mappers;

import dto.reader.CreateReaderResponseDTO;
import dto.reader.ReaderResponseDTO;
import entities.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReaderMapper {

    ReaderMapper INSTANCE = Mappers.getMapper(ReaderMapper.class);

    ReaderResponseDTO toResponseDTO(Reader reader);

    CreateReaderResponseDTO toCreateResponseDTO(Reader reader);

    List<ReaderResponseDTO> toResponseDTOList(List<Reader> readers);
}
