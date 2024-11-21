package dao;

import utils.TestDatabaseConnector;

import javax.sql.DataSource;

abstract class BaseDAOTest {

    private static final DataSource DATA_SOURCE = TestDatabaseConnector.getDataSource();

    final AuthorDAO authorDAO = new AuthorDAO(DATA_SOURCE);
    final ReaderDAO readerDAO = new ReaderDAO(DATA_SOURCE);
    final BookDAO bookDAO = new BookDAO(DATA_SOURCE);
    final ReviewDAO reviewDAO = new ReviewDAO(DATA_SOURCE, readerDAO);
}
