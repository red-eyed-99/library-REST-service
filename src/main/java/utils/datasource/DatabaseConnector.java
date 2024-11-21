package utils.datasource;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DatabaseConnector {

    private static final HikariDataSource DATA_SOURCE;

    static {
        var properties = getDatabaseProperties();

        DATA_SOURCE = new HikariDataSource();

        DATA_SOURCE.setDriverClassName(properties.getProperty("driver"));
        DATA_SOURCE.setJdbcUrl(properties.getProperty("url"));
        DATA_SOURCE.setUsername(properties.getProperty("username"));
        DATA_SOURCE.setPassword(properties.getProperty("password"));
    }

    private DatabaseConnector() {
    }

    private static Properties getDatabaseProperties() {
        var properties = new Properties();

        try (var reader = new InputStreamReader(Objects.requireNonNull(
                DatabaseConnector.class.getResourceAsStream("/db.properties")))) {

            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
}
