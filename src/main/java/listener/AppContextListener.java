package listener;

import dao.AuthorDAO;
import dao.BookDAO;
import dao.ReaderDAO;
import dao.ReviewDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import services.AuthorService;
import services.BookService;
import services.ReaderService;
import services.ReviewService;
import utils.datasource.DatabaseConnector;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var context = sce.getServletContext();

        var dataSource = DatabaseConnector.getDataSource();

        var authorDAO = new AuthorDAO(dataSource);
        var bookDAO = new BookDAO(dataSource);
        var readerDAO = new ReaderDAO(dataSource);
        var reviewDAO = new ReviewDAO(dataSource, readerDAO);

        var authorService = new AuthorService(authorDAO);
        var bookService = new BookService(bookDAO, authorDAO);
        var readerService = new ReaderService(readerDAO, bookDAO);
        var reviewService = new ReviewService(reviewDAO, readerDAO, bookDAO);

        context.setAttribute("authorService", authorService);
        context.setAttribute("bookService", bookService);
        context.setAttribute("readerService", readerService);
        context.setAttribute("reviewService", reviewService);
    }
}
