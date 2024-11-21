package dao;

import entities.Reader;
import exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.RandomPhoneGenerator;
import utils.TestDatabaseConnector;

import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ReaderDAOTest {

    private final ReaderDAO readerDAO = new ReaderDAO(TestDatabaseConnector.getDataSource());

    @Test
    @DisplayName("Create reader")
    void shouldAddReaderToDatabase() throws SQLException {
        var expectedReader = new Reader.ReaderBuilder("dummy", "dummy", RandomPhoneGenerator.generate()).build();

        readerDAO.create(expectedReader);

        assertAll(
                () -> assertNotNull(expectedReader.getId()),
                () -> assertEquals(expectedReader, readerDAO.findById(expectedReader.getId()))
        );
    }

    @Test
    @DisplayName("Find all readers")
    void shouldReturnAllReadersFromDatabase() throws SQLException {
        var expectedReaders = new HashSet<Reader>();

        for (int i = 0; i < 3; i++) {
            var reader = new Reader.ReaderBuilder("dummy", "dummy", RandomPhoneGenerator.generate()).build();

            expectedReaders.add(reader);

            readerDAO.create(reader);
        }

        var readers = readerDAO.findAll();

        assertTrue(readers.containsAll(expectedReaders));
    }

    @Test
    @DisplayName("Update reader phone")
    void shouldUpdateReaderPhoneInDatabase() throws SQLException {
        var reader = new Reader.ReaderBuilder("dummy", "dummy", RandomPhoneGenerator.generate()).build();

        readerDAO.create(reader);

        reader.setPhone("+7(444)-444-44-44");

        readerDAO.updatePhone(reader);

        var updatedReader = readerDAO.findById(reader.getId());

        assertAll(
                () -> assertEquals(reader.getId(), updatedReader.getId()),
                () -> assertEquals("+7(444)-444-44-44", updatedReader.getPhone())
        );
    }

    @Test
    @DisplayName("Delete reader by id")
    void shouldDeleteReaderFromDatabase() throws SQLException {
        var reader = new Reader.ReaderBuilder("dummy", "dummy", RandomPhoneGenerator.generate()).build();

        readerDAO.create(reader);
        readerDAO.delete(reader.getId());

        assertThrows(NotFoundException.class, () -> readerDAO.findById(reader.getId()));
    }
}
