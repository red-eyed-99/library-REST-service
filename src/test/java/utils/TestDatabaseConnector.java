package utils;

import com.zaxxer.hikari.HikariDataSource;
import org.testcontainers.containers.MySQLContainer;
import utils.datasource.DatabaseConnector;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Properties;

public class TestDatabaseConnector {

    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.3");

    private static final HikariDataSource DATA_SOURCE;

    static {
        mySQLContainer.withInitScripts("db_scripts/create_tables.sql");
        mySQLContainer.start();

        var properties = getDatabaseProperties();

        DATA_SOURCE = new HikariDataSource();

        DATA_SOURCE.setDriverClassName(properties.getProperty("driver"));
        DATA_SOURCE.setJdbcUrl(mySQLContainer.getJdbcUrl());
        DATA_SOURCE.setUsername(mySQLContainer.getUsername());
        DATA_SOURCE.setPassword(mySQLContainer.getPassword());
    }

    private TestDatabaseConnector() {
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
