package services;

import dao.BookDAO;
import dao.ReaderDAO;
import dto.reader.CreateReaderRequestDTO;
import dto.reader.CreateReaderResponseDTO;
import dto.reader.ReaderResponseDTO;
import dto.reader.UpdateReaderPhoneDTO;
import entities.Reader;
import utils.mappers.ReaderMapper;

import java.sql.SQLException;
import java.util.List;

public class ReaderService {

    private final ReaderDAO readerDAO;
    private final BookDAO bookDAO;

    private final ReaderMapper mapper = ReaderMapper.INSTANCE;

    public ReaderService(ReaderDAO readerDAO, BookDAO bookDAO) {
        this.readerDAO = readerDAO;
        this.bookDAO = bookDAO;
    }

    public CreateReaderResponseDTO create(CreateReaderRequestDTO createReaderRequestDTO) throws SQLException {
        var firstName = createReaderRequestDTO.getFirstName();
        var lastName = createReaderRequestDTO.getLastName();
        var phone = createReaderRequestDTO.getPhone();

        var reader = new Reader.ReaderBuilder(firstName, lastName, phone).build();

        readerDAO.create(reader);

        return mapper.toCreateResponseDTO(reader);
    }

    public List<ReaderResponseDTO> findAll() throws SQLException {
        List<Reader> readers = readerDAO.findAll();

        return mapper.toResponseDTOList(readers);
    }

    public ReaderResponseDTO findById(Long id) throws SQLException {
        var reader = readerDAO.findById(id);

        return mapper.toResponseDTO(reader);
    }

    public UpdateReaderPhoneDTO updatePhone(UpdateReaderPhoneDTO updateReaderPhoneDTO) throws SQLException {
        var id = updateReaderPhoneDTO.getId();
        var phone = updateReaderPhoneDTO.getPhone();

        var reader = new Reader.ReaderBuilder(id, phone).build();

        readerDAO.updatePhone(reader);

        return updateReaderPhoneDTO;
    }

    public void delete(Long id) throws SQLException {
        readerDAO.delete(id);
    }

    public ReaderResponseDTO addBookToReader(Long readerId, Long bookId) throws SQLException {
        var reader = readerDAO.findById(readerId);
        var book = bookDAO.findById(bookId);

        readerDAO.addBookToReader(readerId, bookId);

        reader.getBooks().add(book);

        return mapper.toResponseDTO(reader);
    }
}
